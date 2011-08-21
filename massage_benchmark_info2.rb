#!/usr/bin/env ruby

require 'fileutils'
require 'date'

dir_name = "/var/log/seph_benchmarks"

FileUtils.mkdir_p "build/performance_report"

def read_bench_file(f, names, collated_results)
  raw_results = Hash.new do |h, k|
    h[k] = []
  end

  open(f) do |f|
    f.each_line do |line|
      name, time_taken = line.chomp.split(/\|/)
      raw_results[name] << Float(time_taken)
    end
  end

  missing_names = names.dup
  raw_results.each do |name, times|
    missing_names.delete(name)
    collated_results[name] << times.min
  end
  missing_names.each do |name|
    collated_results[name] << nil
  end
end

groups = {}
all_names = []
Dir["bench/bench_*.sp"].each do |f|
  current_bench = []
  open(f) do |of|
    of.each_line do |l|
      if l =~ /^benchmark\("(.*?)",/
        current_bench << $1
      end
    end
    all_names |= current_bench
    groups[f] = current_bench.sort
  end
end

collated_results = Hash.new do |h, k|
  h[k] = []
end

entries = []
Dir["#{dir_name}/all_bench_results*"].sort.each do |f|
  f =~ /all_bench_results-(.*?)$/
  entries << $1
  read_bench_file(f, all_names, collated_results)
end

index = 0
git_info = entries.map do |rev|
  who, _when, what = `git show -s --format=format:"%ae|%ad|%s" #{rev}`.split(/\|/)
  res = [rev, who, _when, DateTime.parse(_when), what, index]
  index += 1
  res
end.sort_by do |v|
  v[3]
end




group_index = 0
groups.keys.sort.each do |group|
  unless groups[group].empty?
    out_file = File.basename(group, ".sp")

    File.open("build/performance_report/#{out_file}_data.xml", "w") do |data_file|
      data_file.puts "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
      data_file.puts "<chart>"

      data_file.puts "  <series>"
      git_info.each_with_index do |gi, ix|
        data_file.puts "    <value xid=\"#{ix}\">#{gi[0]}</value>"
      end
      data_file.puts "  </series>"
      
      data_file.puts "  <graphs>"

      gid = 0
      groups[group].sort.each do |bname|
        unless collated_results[bname].compact.empty?
          data_file.puts "    <graph gid=\"#{gid+1}\" title=\"#{bname.gsub("&", "&amp;amp;").gsub(">", "&amp;gt;").gsub("<", "&amp;lt;")}\" balloon_text=\"{value} sec\">"
          git_info.each_with_index do |gi, ix|
            val = collated_results[bname][gi[5]]
            data_file.puts "      <value xid=\"#{ix}\" description=\" seconds\" url=\"https://github.com/seph-lang/seph/commit/#{gi[0]}\">#{val}</value>" if val
          end
          
          data_file.puts "    </graph>"

          gid += 1
        end
      end
      
      data_file.puts "    <graph gid=\"1\" title=\"revision\" hidden=\"true\" visible_in_legend=\"true\" color=\"#FFFFFF\" color_hover=\"#FFFFFF\" selected=\"false\">"
      git_info.each_with_index do |gi, ix|
        data_file.puts "      <value xid=\"#{ix}\" description=\"#{gi[0]} &amp;nbsp;- &amp;nbsp;#{gi[1]}&lt;br&gt;#{gi[2]}&lt;br&gt;(click on chart for the changeset)&lt;br&gt;&lt;br&gt;#{gi[4]}\" url=\"https://github.com/seph-lang/seph/commit/#{gi[0]}\">0</value>"
      end
      data_file.puts "    </graph>"
      
      data_file.puts "  </graphs>"

      data_file.puts <<LABEL
  <labels> 
    <label lid="0"> 
      <y>40</y> 
      <width>640</width> 
      <text_size>16</text_size> 
      <align>center</align> 
      <text> 
        <![CDATA[<b>Trunk - Best microbenchmark time - #{out_file}.sp</b>]]>
      </text>        
    </label>    
  </labels> 
</chart>
LABEL

    end
  end
end


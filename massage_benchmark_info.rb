#!/usr/bin/env ruby

require 'fileutils'
require 'tempfile'

dir_name = "/var/log/seph_benchmarks"
current_timestamp = Time.now.strftime("%Y_%m_%d_%H_%M")

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

open("build/performance_report/index.html", "w") do |ixh|
  ixh.puts <<HTML
<html>
  <head>
    <title>Seph performance report (#{current_timestamp.gsub("_", "-")})</title>
  </head>
  <body>
    <h1>Seph performance report (#{current_timestamp.gsub("_", "-")})</h1>
HTML

  group_index = 0
  groups.keys.sort.each do |group|
    unless groups[group].empty?
      Tempfile.open("seph_benchmark_plot_load") do |pf|
        pf.puts <<STR
set terminal svg
set xdata time
set timefmt "%Y-%m-%d-%H-%M"
set output "build/performance_report/benchmark_group_#{group_index}.svg"
# time range must be in same format as data file
set xrange ["#{entries.first.gsub("_", "-")}":"#{entries.last.gsub("_", "-")}"]
set grid
set xlabel "Timestamp"
set ylabel "Best time (s)"
set title "Benchmark: #{group.gsub("&", "&amp;").gsub(">", "&gt;").gsub("<", "&lt;")}"
set key left box
STR
        first = true

        plot = ""
        
        table = "<table border='1'>"
        
        Tempfile.open("seph_benchmark_data") do |f|
          ix = 0
          groups[group].sort.each do |bname|
            unless collated_results[bname].compact.empty?
              table << "#{first ? "<tr>#{bname.gsub("&", "&amp;").gsub(">", "&gt;").gsub("<", "&lt;")}</tr><tr>" : "</tr><tr>"}"
              entries.zip(collated_results[bname]).each do |timestamp, entry|
                f.puts "#{timestamp.gsub("_", "-")}\t#{entry}"
                table << "<td>#{entry}</td>"
              end
              f.puts ""
              f.puts ""

              plot << "#{first ? "plot " : ", "}\"#{f.path}\" using 1:2 index #{ix} title \"#{bname.gsub("&", "&amp;").gsub(">", "&gt;").gsub("<", "&lt;")}\" with linespoints"
              first = false
              ix += 1
            end
          end

          pf.puts plot
          
          f.flush
          pf.flush

          unless first
            ixh.puts "<img src=\"benchmark_group_#{group_index}.svg\"/>"
            ixh.puts "#{table}</tr></table>"
            system("cat #{pf.path} | gnuplot")
            group_index += 1
          end
        end
      end  
    end
  end
  ixh.puts "  </body>"
  ixh.puts "</html>"
end


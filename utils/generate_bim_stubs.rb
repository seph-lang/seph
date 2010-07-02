#!/usr/bin/env ruby

require 'erb'
require 'fileutils'

potential_java_files = Dir["src/main/**/*.java"]

stubs_to_generate = potential_java_files.inject([]) do |result, current_file|
  File.open(current_file) do |f|
    f.each_line do |l|
      case l
        when /^@SephSingleton/: result << [:singleton, current_file[%r[^.*(?:/|\\)(.*?)\.java$], 1]]; break
        when /^@SephKind/: result << [:kind, current_file[%r[^.*(?:/|\\)(.*?)\.java$], 1]]; break
      end
    end
  end
  result
end

TEMPLATE_SINGLETON = ERB.new(File.read("templates/bim_singleton.java.erb"))
TEMPLATE_KIND      = ERB.new(File.read("templates/bim_kind.java.erb"))

class StubGenerator
  def initialize(type, name)
    @type, @name = type, name
    @template = case type
                  when :kind: TEMPLATE_KIND
                  when :singleton: TEMPLATE_SINGLETON
                end
  end

  def render
    @template.result(binding)
  end
end

FileUtils.mkdir_p "src/gen/seph/lang/bim"
stubs_to_generate.each do |type, name|
  puts "Generating #{type} stub for #{name} as: seph.lang.bim.#{name}Base"
  File.open("src/gen/seph/lang/bim/#{name}Base.java", "w") do |f|
    f.write StubGenerator.new(type, name).render
  end
end

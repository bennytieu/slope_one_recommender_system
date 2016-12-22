#!/usr/bin/perl -w
use warnings;
use strict;

# concatenate.pl
# Concatenates the Netflix Prize traning_set files into one single file.
#

# Quit unless we have the correct number of command-line args
if (($#ARGV+1) != 2) {
    print "Please enter right number of arguments: 'perl scriptname.pl [Directory of Raw Data] [Output directory]'\n";
    exit;
}

# The directory to scan containing the raw files 
my $dirRaw = $ARGV[0];
my $dirOutput = $ARGV[1];

opendir DIR, $dirRaw 
	or die("Could not open $dirRaw");

my $outString;
my $outFile;
while(my $fname = readdir DIR) {
        my $fname = "$dirRaw/$fname";
        open FILE, $fname 
        	or die("Could not open $fname");
        (my $mid = <FILE>) =~ s/:.*//s;
        while(<FILE>) {
                chomp;
                $outString = qq($mid,).qq($_)."\n";
                open $outFile, ">>", "$dirOutput/concat_raw_data.txt"
                	or die ("Error opening to new file.");
                print {$outFile} $outString;
        }
        close $outFile;
        close FILE;
}
closedir DIR;
print "Done.";
exit;
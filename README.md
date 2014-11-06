KIT: KIR Typing from whole genome sequencing data. 

Software Requirements 

bwa: http://sourceforge.net/projects/bio-bwa/files/ 

samtools: http://sourceforge.net/projects/samtools/files/

Quickstart

		java -jar KITCN.jar
        -r1 data/Quickstart/sample_fir.fastq \
        -r2 data/Quickstart/sample_sec.fastq \
        -s sample
        
		java -jar KITAT.jar
        -r1 data/Quickstart/sample_fir.fastq \
        -r2 data/Quickstart/sample_sec.fastq \
        -s sample \
        -c A_A
        -d data/Quickstart/Temp/ \
        -o data/Quickstart/Out/






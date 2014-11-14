===========
KIT: KIR Typing from whole genome sequencing data. 
===========

Software Requirements 
=========

* bwa: http://sourceforge.net/projects/bio-bwa/files/ 
* samtools: http://sourceforge.net/projects/samtools/files/

Quickstart
=========

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

KIT-CN
=========

KIT-CN predicts the copy number of KIR genes by counting indicative strings. KIT-CN uses the count of indicative strings in the sample and compares it to the expected count of indicative strings for one copy number of each gene to infer gene copy number. To generate the expected count of indicative strings for one copy number of each gene, KIT-CN uses a database of known KIR templates, this is provided in data/. Use of custom database will be available soon. 

		java -jar KITCN.jar [OPTIONS]

Options
-------------
	-r1		fasta/q file of paired end reads (first paired end)
	-r2 	fasta/q file of paired end reads (second paired end)
	-r 		fasta/q file of paired end reads (unpaired reads)
	-b 		bam/sam file of reads
	-bai 	bam index file 
	-s		sample name







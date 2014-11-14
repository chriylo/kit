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


=======
KIT-AT
=========

KIT-AT predicts the allele for each copy of each gene using a maximum likelihood approach choosing among a database of alleles. There are three steps to KIT-AT. For each gene, these three steps are run. The first step is to filter the wgs reads sampled from the gene region of interest using unique indicative strings. Then the filtered reads are aligned to a gene reference. Finally the reads are evaluated at the polymorphic sites in order to choose the most likely set of alleles corresponding to the reads. KIT-AT uses a database of known KIR alleles. Use of custom database will be available soon. 

		java -jar KITAT.jar [OPTIONS]

Options
-------------
	-r1		fasta/q file of paired end reads (first paired end)
	-r2 	fasta/q file of paired end reads (second paired end)
	-r 		fasta/q file of paired end reads (unpaired reads)
	-b 		bam/sam file of reads
	-bai 	bam index file 
	-s		sample name
	-c 		kir type (output from KIT-CN)
	-d 		temporary output directory
	-o 		output directory




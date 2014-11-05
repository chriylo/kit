KIT: KIR Typing from whole genome sequencing data. 

Software Requirements
bwa: http://sourceforge.net/projects/bio-bwa/files/
samtools: http://sourceforge.net/projects/samtools/files/

Quickstart

		java -jar KITCN.jar
        -r1 Data/Quickstart/sample_fir.fastq \
        -r2 Data/Quickstart/sample_sec.fastq \
        -s sample_name
        
    java -jar KITAT.jar \
        -r1 Data/Quickstart/sample_fir.fastq \
        -r2 Data/Quickstart/sample_sec.fastq \
        -s sample_name \
        -c A_A
        -d Data/Quickstart/Temp/ \
        -o Data/Quickstart/Out/






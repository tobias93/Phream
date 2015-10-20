all: 	documentation.pdf

continuous:
	latexmk -pdf -pvc -pdflatex="pdflatex --shell-escape --interaction=nonstopmode %O %S" documentation

documentation.pdf:
	latexmk -pdf -pdflatex="pdflatex --shell-escape %O %S" documentation

clean:
	latexmk -C documentation
	rm -f documentation.pdfsync
	rm -rf *~ *.tmp
	rm -f *.bbl *.blg *.aux *.end *.fls *.log *.out *.fdb_latexmk 


kidiplom: %: %.tex
	@pdflatex $@
	@pdflatex $@
	@biber $@
	@makeglossaries $@
	@makeindex $@.idx
	@pdflatex $@
	@pdflatex $@

clean:
	@rm -v -f *.glsdefs *.bcf *.lo* *.aux *.ind *.idx *.ilg *.toc *.acn *.run.xml *-blx.bib *.ist *.glo  *.blg *.bbl  *.gls *.glg *.alg *.acr

dist:
	@mkdir kidiplom
	@cp -r -f kidiplom.pdf kidiplom.tex kidiplom.cls kibase.sty graphics *bib *bx Makefile README.txt LICENSE kidiplom/
	@rm -f kidiplom.zip
	@zip -r kidiplom.zip kidiplom
	@rm -r -f kidiplom

KIdiplom
========

V tomto adresáři je styl KIdiplom pro text závěrečné (bakalářské a
diplomové) práce na Katedře informatiky Přírodovědecké fakulty
Univerzity Palackého v Olomouci (KI PřF UP v Olomouci) v typografickém systému
LaTeX, včetně ukázkového textu práce, který je zároveň dokumentací
stylu.

Soubory a adresáře:

kidiplom.tex

= zdrojový text, využívající styl KIdiplom, ukázkového textu závěrečné
práce a zároveň dokumentace stylu, doporučeno použít jako šablonu pro
text vlastní práce, instrukce pro použití stylu KIdiplom a překlad
zdrojového textu jsou v souboru ve formě komentářů, soubor je v
kódování UTF-8 (doporučeno zachovat)

kidiplom.pdf

= přeložený PDF dokument ukázkového textu kidiplom.tex

kidiplom.cls

= třída dokumentu v LaTeXu představující styl KIdiplom, pro použití ve
zdrojových textech dokumentů využívajících styl, doporučeno soubor
ponechat ve stejném adresáři, jako soubor kidiplom.tex, soubor
neupravujte!

kibase.sty

= dodatečný styl využívaný třídou kidiplom.cls, doporučeno soubor
ponechat ve stejném adresáři, jako soubor kidiplom.tex, soubor
neupravujte!

bibliografie.bib

= ukázková bibliografie dokumentu kidiplom.tex pro BibLaTeX

graphics/

= adresář s obrázky záhlaví a loga UP na titulní straně dokumentů
používajících styl KIdiplom, doporučeno adresář ponechat ve stejném
adresáři, jako soubor kidiplom.tex, soubory v adresáři neupravujte!

*bx

= implementace stylů bibliografie pro BibLaTeX podle normy ISO 690,
doporučeno soubory ponechat ve stejném adresáři, jako soubor
kidiplom.tex, soubory neupravujte!

Makefile

= předpis pro program Make obsahující (mj.) příkazy pro překlad
zdrojového textu kidiplom.tex do PDF dokumentu kidiplom.pdf, soubor
musí být ve stejném adresáři, jako soubor kidiplom.tex

LICENSE

= text licence stylu KIdiplom

README.txt

= tento text

Styl byl testován s distribucí LaTeXu TeX Live (verze 2021). Pro svoji
plnou funkčnost vyžaduje následující (a jimy rekurzivně vyžadované)
dodatečné styly (balíky), které, pokud nejsou součástí standardní instalace
distribuce, lze v rámci distribuce doinstalovat:

amsbsy
amsmath
amssymb
amsthm
babel
biblatex *
bookmark
csquotes
euscript
fontenc
geometry
glossaries *
graphicx
hyperref
ifpdf
ifthen
inputenc
iwona *
kvoptions
lastpage
lmodern *
listings
makeidx *
multicol *
savesym *
thmtools
xcolor

* = vyžadování závisí na použitých volbách stylu

Licence stylu KIdiplom je GNU GPLv3 nebo novější.

Primární zdroj stylu pro použití na text odevzdávaných závěrečných
prací na KI PřF UP v Olomouci je
 
http://www.inf.upol.cz/studium

Chyby v souvislosti s tímto použitím reportuje na první kontakt níže.

Styl je vyvíjen na https://github.com/martinrotter/kistyles. Zde
můžete reportovat technické chyby.

Kontakt:
  Jan Outrata <jan.outrata@upol.cz>          (univerzitní oficiální zastoupení, spoluautor stylu)
  Martin Rotter <rotter.martinos@gmail.com>  (autor stylu)

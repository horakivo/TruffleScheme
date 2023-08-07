%%%  KI PřF UP v Olomouci Diplom Class
%%%  Copyright (C) 2012 Martin Rotter, <rotter.martinos@gmail.com>
%%%  Copyright (C) 2014 Jan Outrata, <jan.outrata@upol.cz>

\NeedsTeXFormat{LaTeX2e}
\ProvidesClass{kidiplom}[2012/09/09 KI PrF UP v Olomouci Diploma Class]
\ClassWarningNoLine{kidiplom}{[kidiplom] You are using kidiplom class 1.2}

%% Základní třída a velikost písma 12pt.
\LoadClass[12pt]{article}

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% Parametry.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

\RequirePackage{kvoptions}

\SetupKeyvalOptions {
	family=kid,
	prefix=kid@
}

%% [init]{name}[default]
%% diplomová práce, jinak bakalářská
\DeclareBoolOption{master}
%% studijní program/obor a specializace autora: pro diplomovou práci
%% infoi, infui, inf, ainfpst, ainf, uinf a binf, jinak ainfvs, pro
%% bakalářskou práci infoi, inf, itp, itk, ainfp, ainfk, infv a binf,
%% jinak infpvs
\DeclareStringOption[infpvs]{program}[infpvs]
\DeclareStringOption[false]{printversion}[true]
\DeclareStringOption[true]{joinlists}[true]
\DeclareStringOption[true]{figures}[true]
\DeclareStringOption[true]{tables}[true]
\DeclareStringOption[false]{theorems}[true]
\DeclareStringOption[false]{sourcecodes}[true]
\DeclareStringOption[false]{biblatex}[true]
\DeclareStringOption[czech]{language}[czech]
\DeclareStringOption[false]{glossaries}[true]
\DeclareStringOption[false]{index}[true]
\DeclareStringOption[serif]{font}[serif]
\DeclareStringOption[utf8]{encoding}[utf8]
\DeclareStringOption[utf8]{bibencoding}[utf8]

%% Nerozpoznaný parametr hodí chybu.
\DeclareDefaultOption{\ClassWarningNoLine{kidiplom}{[kidiplom] Option '\CurrentOption' was not recognized}}

\ProcessKeyvalOptions*

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% Balíky.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%% KI PřF UP v Olomouci Base styl.
\RequirePackage[
  printversion=\kid@printversion,
  joinlists=\kid@joinlists,
  figures=\kid@figures,
  tables=\kid@tables,
  theorems=\kid@theorems,
  sourcecodes=\kid@sourcecodes,
  biblatex=\kid@biblatex,
  language=\kid@language,
  glossaries=\kid@glossaries,
  index=\kid@index,
  font=\kid@font,
  encoding=\kid@encoding,
  bibencoding=\kid@bibencoding
]{kibase}

%% Oboustranná sazba není povolená.
\ifthenelse{\boolean{@twoside}}{
  \ClassError{kidiplom}{Two-sided document mode is not allowed for kidiplom.}
}{}

%% Geometrie papíru.
\RequirePackage[
  paper=a4paper,
  driver=pdftex,
  top=3.5cm,
  left=4cm,
  bottom=3.5cm,
  width=1.05\textwidth
]{geometry}

%% Číslo poslední strany: label LastPage.
\RequirePackage{lastpage}

%% AMS symboly.
\RequirePackage{amsbsy}
\RequirePackage{amssymb}
\RequirePackage{euscript}

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% Dynamické texty.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%% Čeština.
\def\@bachelorcz{bakal\'a\v rsk\'a pr\'ace}
\def\@mastercz{diplomov\'a pr\'ace}
\def\@programinfcz{Informatika}
\def\@programainfcz{Aplikovan\'a informatika}
\def\@programitcz{Informa\v cn\'i technologie}
\def\@programinfvcz{Informatika pro vzd\v el\'av\'an\'i}
\def\@programuinfcz{U\v citelstv\'i informatiky pro
        st\v redn\'i \v skoly}
\def\@programbinfcz{Bioinformatika}
\def\@specialoicz{Obecn\'a informatika}
\def\@specialpvscz{Programov\'an\'i a v\'yvoj software}
\def\@specialuicz{Um\v el\'a inteligence}
\def\@specialvscz{V\'yvoj software}
\def\@specialpstcz{Po\v c\'ita\v cov\'e syst\'emy a technologie}
\def\@presentformcz{prezen\v cn\'i forma}
\def\@combinedformcz{kombinovan\'a forma}
\def\@czlangcz{\v cesk\'y}
\def\@enlangcz{anglick\'y}
\def\@sklangcz{slovensk\'y}

\def\@textbibinfocz{Bibliografick\'e \'udaje}
\def\@texttitlecz{N\'azev pr\'ace}
\def\@textauthorcz{Autor}
\def\@textthesistypecz{Typ pr\'ace}
\def\@textdeptcz{Pracovi\v st\v e}
\def\@textyearofsubmitcz{Rok obhajoby}
\def\@textpagescz{Po\v cet stran}
\def\@textsupplcz{P\v r\'ilohy}
\def\@textsupervisorcz{Vedouc\'i pr\'ace}
\def\@textprogramcz{Studijn\'i program}
%\def\@textfieldcz{Studijn\'i obor}
\def\@textspecialcz{Specializace}
\def\@textlangcz{Jazyk práce}
\def\@textanotcz{Anotace}
\def\@textkeywordscz{Klí\v cov\'a slova}
\def\@textconclusionscz{Z\'av\v er}

%% Angličtina.
\def\@bacheloren{bachelor thesis}
\def\@masteren{master thesis}
\def\@programinfen{Computer Science}
\def\@programainfen{Applied Computer Science}
\def\@programinfven{Computer Science for Education}
\def\@programiten{Information Technologies}
\def\@programuinfen{Teaching Training in Computer Science for Secondary Schools}
\def\@programbinfen{Bioinformatics}
\def\@specialoien{General Computer Science}
\def\@specialpvsen{Programming and Software Development}
\def\@specialuien{Artificial Intelligence}
\def\@specialvsen{Software Development}
\def\@specialpsten{Computer Systems and Technologies}
\def\@presentformen{full-time form}
\def\@combinedformen{combined form}
\def\@czlangen{Czech}
\def\@enlangen{English}
\def\@sklangen{Slovak}

\def\@textbibinfoen{Bibliographic info}
\def\@texttitleen{Title}
\def\@textauthoren{Author}
\def\@textthesistypeen{Thesis type}
\def\@textdepten{Department}
\def\@textyearofsubmiten{Year of defense}
\def\@textpagesen{Page count}
\def\@textsupplen{Supplements}
\def\@textsupervisoren{Supervisor}
\def\@textprogramen{Study program}
%\def\@textfielden{Study field}
\def\@textspecialen{Specialization}
\def\@textlangen{Thesis language}
\def\@textanoten{Synopsis}
\def\@textkeywordsen{Keywords}
\def\@textconclusionsen{Conclusions}

%% Slovenština.
\def\@textanotsk{Anot\'acia}
\def\@textkeywordssk{K\v lú\v cov\'e slov\'a}
\def\@textconclusionssk{Z\'aver}

%% Typ práce a studijní obor autora (podle typu práce).
\ifthenelse{\boolean{kid@master}}{
  %% Magisterská.
  \def\@thesistypecz{\@mastercz}
  \def\@thesistypeen{\@masteren}
  \ifthenelse{\equal{\kid@program}{infoi}}{
    \def\@programen{\@programinfen, \@textspecialen: \@specialoien}
    \def\@programcz{\@programinfcz, \@textspecialcz: \@specialoicz}
  }{
    \ifthenelse{\equal{\kid@program}{infui}}{
      \def\@programen{\@programinfen, \@textspecialen: \@specialuien}
      \def\@programcz{\@programinfcz, \@textspecialcz: \@specialuicz}
    }{
      \ifthenelse{\equal{\kid@program}{ainfpst}}{
        \def\@programen{\@programainfen, \@textspecialen: \@specialpsten}
        \def\@programcz{\@programainfcz, \@textspecialcz: \@specialpstcz}
      }{
        \ifthenelse{\equal{\kid@program}{uinf}}{
          \def\@programen{\@programuinfen, \@presentformen}
          \def\@programcz{\@programuinfcz, \@presentformcz}
        }{
          \ifthenelse{\equal{\kid@program}{binf}}{
            \def\@programen{\@programbinfen, \@presentformen}
            \def\@programcz{\@programbinfcz, \@presentformcz}
          }{
            \ifthenelse{\equal{\kid@program}{inf}}{
              \def\@programen{\@programinfen, \@presentformen}
              \def\@programcz{\@programinfcz, \@presentformcz}
            }{
              \ifthenelse{\equal{\kid@program}{ainf}}{
                \def\@programen{\@programainfen, \@presentformen}
                \def\@programcz{\@programainfcz, \@presentformcz}
              }{
                % \ifthenelse{\equal{\kid@program}{ainfvs}}
                \def\@programen{\@programainfen, \@textspecialen: \@specialvsen}
                \def\@programcz{\@programainfcz, \@textspecialcz: \@specialvscz}
              }
            }
          }
        }
      }
    }
  }
}{
  %% Bakalářská.
  \def\@thesistypecz{\@bachelorcz}
  \def\@thesistypeen{\@bacheloren}
  \ifthenelse{\equal{\kid@program}{infoi}}{
    \def\@programen{\@programinfen, \@textspecialen: \@specialoien}
    \def\@programcz{\@programinfcz, \@textspecialcz: \@specialoicz}
  }{
    \ifthenelse{\equal{\kid@program}{itp}}{
      \def\@programen{\@programiten, \@presentformen}
      \def\@programcz{\@programitcz, \@presentformcz}
    }{
      \ifthenelse{\equal{\kid@program}{itk}}{
        \def\@programen{\@programiten, \@combinedformen}
        \def\@programcz{\@programitcz, \@combinedformcz}
      }{
        \ifthenelse{\equal{\kid@program}{infv}}{
          \def\@programen{\@programinfven, \@presentformen}
          \def\@programcz{\@programinfvcz, \@presentformcz}
        }{
          \ifthenelse{\equal{\kid@program}{binf}}{
            \def\@programen{\@programbinfen, \@presentformen}
            \def\@programcz{\@programbinfcz, \@presentformcz}
          }{
            \ifthenelse{\equal{\kid@program}{inf}}{
              \def\@programen{\@programinfen, \@presentformen}
              \def\@programcz{\@programinfcz, \@presentformcz}
            }{
              \ifthenelse{\equal{\kid@program}{ainfp}}{
                \def\@programen{\@programainfen, \@presentformen}
                \def\@programcz{\@programainfcz, \@presentformcz}
              }{
                \ifthenelse{\equal{\kid@program}{ainfk}}{
                  \def\@programen{\@programainfen, \@combinedformen}
                  \def\@programcz{\@programainfcz, \@combinedformcz}
                }{
                  % \ifthenelse{\equal{\kid@program}{infpvs}}
                  \def\@programen{\@programinfen, \@textspecialen: \@specialpvsen}
                  \def\@programcz{\@programinfcz, \@textspecialcz: \@specialpvscz}
                }
              }
            }
          }
        }
      }
    }
  }
}

%% Angličtina.
\ifthenelse{\equal{\kid@language}{english}}{
  \def\@thesistype{\@thesistypeen}
  \def\@textprogram{\@textprogramen}
  \def\@program{\@programen}
  \def\@textsupervisor{\@textsupervisoren}
  \def\@langcz{\@enlangcz}
  \def\@langen{\@enlangen}
  \def\@textanot{\@textanoten}
  \def\@textkeywords{\@textkeywordsen}
  \def\@textaffirm{By submitting this text its author declares that he/she has completed this thesis including its appendices on his/her own and used solely the sources cited in the text and included in the bibliography list.}
  \def\@textsubmitdate{date of thesis submission}
  \def\@textsigniture{author's signature}
}{
  \relax
}

%% Čeština.
\ifthenelse{\equal{\kid@language}{czech}}{
  \def\@thesistype{\@thesistypecz}
  \def\@textprogram{\@textprogramcz}
  \def\@program{\@programcz}
  \def\@textsupervisor{\@textsupervisorcz}
  \def\@langcz{\@czlangcz}
  \def\@langen{\@czlangen}
  \def\@textanot{\@textanotcz}
  \def\@textkeywords{\@textkeywordscz}
  \def\@textaffirm{Odevzd\'an\'im tohoto textu jeho autor/ka m\'istop\v r\'ise\v zn\v e prohla\v suje, \v ze celou pr\'aci v\v cetn\v e p\v r\'iloh vypracoval/a samostatn\v e a za pou\v zit\'i pouze zdroj\r u citovan\'ych v~textu pr\'ace a uveden\'ych v seznamu literatury.}
  \def\@textsubmitdate{datum odevzd\'an\'i pr\'ace}
  \def\@textsigniture{podpis autora}
}{
  \relax
}

%% Slovenština.
\ifthenelse{\equal{\kid@language}{slovak}}{
  \def\@thesistype{\@thesistypecz}
  \def\@textprogram{\@textprogramcz}
  \def\@program{\@programcz}
  \def\@textsupervisor{\@textsupervisorcz}
  \def\@langcz{\@sklangcz}
  \def\@langen{\@sklangen}
  \def\@textanot{\@textanotsk}
  \def\@textkeywords{\@textkeywordssk}
  \def\@textaffirm{Odevzdan\'im tohto textu jeho autor/ka cestne vyhlasuje, \v ze cel\'u pr\'acu vr\'atane pr\'iloh vypracoval/a samostatne a za pou\v zitia iba zdrojov spom\'inan\'ych v~texte pr\'ace a uveden\'ych v zozname literat\'ury.
}
  \def\@textsubmitdate{d\'atum odovzdania pr\'ace}
  \def\@textsigniture{podpis autora}
}{
  \relax
}

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% Prostředí pro závěry.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

\newenvironment{kiconclusions}[1][\kid@language]{
  \clearpage
  \bgroup
  \phantomsection
  \ifthenelse{\equal{#1}{english}}{
    \addcontentsline{toc}{section}{\@textconclusionsen}
    \section*{\@textconclusionsen}
    \begin{otherlanguage}{english}
  }{}
  \ifthenelse{\equal{#1}{czech}}{
    \addcontentsline{toc}{section}{\@textconclusionscz}
    \section*{\@textconclusionscz}
    \begin{otherlanguage}{czech}
  }{}
  \ifthenelse{\equal{#1}{slovak}}{
    \addcontentsline{toc}{section}{\@textconclusionssk}
    \section*{\@textconclusionssk}
    \begin{otherlanguage}{slovak}
  }{}
}{
  \end{otherlanguage}
  \egroup
}

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% Úvodní strany.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%% Makra pro název a podnázev práce.
%\def\title#1{\gdef\@title{#1}}
\renewcommand{\title}[2][\kid@language]{
  \ifthenelse{\equal{#1}{english}}{
    \gdef\@titleen{#2}
  }{
    \gdef\@titlecz{#2}
  }
}

%\def\subtitle#1{\gdef\@subtitle{#1}}
\newcommand{\subtitle}[2][\kid@language]{
  \ifthenelse{\equal{#1}{english}}{
    \gdef\@subtitleen{#2}
  }{
    \gdef\@subtitlecz{#2}
  }
}

\ifthenelse{\equal{\kid@language}{english}}{
  \gdef\@title{\@titleen}
  \gdef\@subtitle{\@subtitleen}
}{
  \gdef\@title{\@titlecz}
  \gdef\@subtitle{\@subtitlecz}
}

%% Makra pro anotaci, klíčová slova a přílohy.
\newcommand{\annotation}[2][\kid@language]{
  \ifthenelse{\equal{#1}{english}}{
    \gdef\@annotationen{#2}
  }{
    \gdef\@annotationcz{#2}
  }
}

\newcommand{\keywords}[2][\kid@language]{
  \ifthenelse{\equal{#1}{english}}{
    \gdef\@keywordsen{#2}
  }{
    \gdef\@keywordscz{#2}
  }
}

\ifthenelse{\equal{\kid@language}{english}}{
  \gdef\@keywords{\@keywordsen}
}{
  \gdef\@keywords{\@keywordscz}
}

\newcommand{\supplements}[2][\kid@language]{
  \ifthenelse{\equal{#1}{english}}{
    \gdef\@supplen{#2}
  }{
    \gdef\@supplcz{#2}
  }
}

%% Makra pro autora, vedoucího a datum odevzdání práce.
\def\author#1{\gdef\@author{#1}}
\def\supervisor#1{\gdef\@supervisor{#1}}
\def\yearofsubmit#1{\gdef\@yearofsubmit{#1}}

%% Makro pro poděkování.
\def\thanks#1{\gdef\@thanks{#1}}

%% Výchozí hodnoty pro předchozí makra.
\title[czech]{N\'azev pr\'ace}
\title[english]{Thesis title}
\subtitle[czech]{}
\subtitle[english]{}
\annotation[czech]{V\'ysti\v zn\'a anotace pr\'ace}
\annotation[english]{Apposite thesis synopsis}
\keywords[czech]{stru\v cn\'a vhodn\'a kl\'i\v cov\'a slova}
\keywords[english]{short appropriate keywords}
\supplements[czech]{elektronická data v úložišti katedry informatiky}
\supplements[english]{electronic data in the storage of department of computer science}
\ifthenelse{\equal{\kid@language}{english}}{
  \author{Author name}
  \supervisor{Supervisor name}
}{
  \author{Jm\'eno autora}
  \supervisor{Jm\'eno vedouc\'iho pr\'ace}
}
\yearofsubmit{\the\year}

%% Číslování stran v českých (a slovenských) textech od 1 do konce.
\ifthenelse{\not\equal{\kid@language}{english}}{
  %% HACK: pro sekvenční fyzické číslování stran, protože
  %% např. \tableofcontents číslování resetuje.
  \let\orgsetcounter=\setcounter
  \renewcommand\setcounter[2]{%
    \def\tempa{#1}%
    \def\tempb{page}%
    \ifx\tempa\tempb\else\orgsetcounter{#1}{#2}\fi}
}{
  \relax
}

%% Makro pro titulní stranu.
\def\maketitle{
%% Záložka.
\hypertarget{ki:paper}{}
\bookmark[dest=ki:paper]{\@title}
%% Římské číslování úvodních stran v anglických textech.
\ifthenelse{\equal{\kid@language}{english}}{
  \pagenumbering{roman}
}{}
%% Číslování stran od 1.
\setcounter{page}{1}

%% Titulní strana.
\begin{titlepage}
%% Záložka.
\hypertarget{ki:title}{}
\bookmark[
  rellevel=1,
  keeplevel,
  dest=ki:title
]{\@texttitle}
\begin{center}
%{\fontfamily{ppl}\selectfont \large \MakeUppercase{\kitextdept} \par
%  \MakeUppercase{\kitextfaculty} \par \MakeUppercase{\kitextuniv} \par}
\ifthenelse{\equal{\kid@language}{english}}{
  \includegraphics[scale=.6]{graphics/kititle-en-nofont}
}{
  \includegraphics[scale=.6]{graphics/kititle-cz-nofont}
}
\vfill
{\LARGE\bfseries{\MakeUppercase{\@thesistype}} \\}
\vfill
{\Large \@title \\[4ex]\large\@subtitle}

\vfill
\centering
\includegraphics{\@logofile}
\vfill

\begin{tabular}{@{}L{6cm}@{}@{\hspace{.8cm}}@{}L{7.2cm}@{}}
{\Large\@yearofsubmit} & {\Large\@author} \\[2ex]
{\small\@textsupervisor:\newline \@supervisor} & {\small\@textprogram: \@program}
\end{tabular}

\end{center}
\end{titlepage}

%% Strana s bibliografickými identifikačními údaji.
\thispagestyle{empty}
{\small
{\bf \@textbibinfocz} \par
\bigskip
\renewcommand{\arraystretch}{1.5}
\begin{tabular}{@{}p{3cm}@{\hspace{.5cm}}@{}p{10cm}@{}}
\@textauthorcz: & \@author \\
\@texttitlecz: & \@titlecz%
\ifthenelse{\equal{\@subtitlecz}{}}{%
  \relax
}{%
\ (\@subtitlecz)
} \\
\@textthesistypecz: & \@thesistypecz \\
\@textdeptcz: & \kitextdeptcz, \kitextfacultycz, \kitextunivcz \\
\@textyearofsubmitcz: & \@yearofsubmit \\
\@textprogramcz: & \@programcz \\
\@textsupervisorcz: & \@supervisor \\
\@textpagescz: & \nohref{\pageref{LastPage}} \\
\@textsupplcz: & \@supplcz \\
\@textlangcz: & \@langcz
\end{tabular}
\par
\vspace*{8ex}
{\bf \@textbibinfoen} \par
\bigskip
\renewcommand{\arraystretch}{1.5}
\begin{tabular}{@{}p{3cm}@{\hspace{.5cm}}@{}p{10cm}@{}}
\@textauthoren: & \@author \\
\@texttitleen: & \@titleen%
\ifthenelse{\equal{\@subtitleen}{}}{%
  \relax
}{%
\ (\@subtitleen)
} \\
\@textthesistypeen: & \@thesistypeen \\
\@textdepten: & \kitextdepten, \kitextfacultyen, \kitextuniven \\
\@textyearofsubmiten: & \@yearofsubmit \\
\@textprogramen: & \@programen \\
\@textsupervisoren: & \@supervisor \\
\@textpagesen: & \nohref{\pageref{LastPage}} \\
\@textsupplen: & \@supplen \\
\@textlangen: & \@langen
\end{tabular}
}

%% Strana s anotací a klíčovými slovy.
\cleardoublepage
\thispagestyle{empty}
\null\vfill
%% Záložka.
\hypertarget{ki:anotation}{}
\bookmark[
  rellevel=1,
  keeplevel,
  dest=ki:anotation
]{\@textanot}
\begin{center}
\ifthenelse{\equal{\kid@language}{english}}{
  \textbf{\@textanotcz}
}{
  \textbf{\@textanot}
}
\end{center}
\bigskip
\emph{\@annotationcz}
\vspace*{4ex}
\begin{center}
\textbf{\@textanoten}
\end{center}
\bigskip
\emph{\@annotationen}
\par
\vspace*{10ex}
\noindent\textbf{\ifthenelse{\equal{\kid@language}{english}}{\@textkeywordscz:}{\@textkeywords:}}
\@keywordscz
\par
\vspace*{2ex}
\noindent\textbf{\@textkeywordsen:} \@keywordsen
\vfill\null

%% Strana s poděkováním a prohlášením.
\clearpage
\thispagestyle{empty}
\null\vfill
\ifthenelse{\equal{\@thanks}{}}{
\relax
}{
\noindent\@thanks
}
\vfill
\noindent\emph{\@textaffirm}\par
%\vspace*{10ex}
%\noindent\@textsubmitdate \hfill \@textsigniture

%% Obsah a seznamy obrázků, tabulek, vět a zdrojových kódů.
\tableofcontents
\@printlists

%% Arabské číslování stran textu v anglických textech.
%% Číslování stran od 1.
\ifthenelse{\equal{\kid@language}{english}}{
  \pagenumbering{arabic}
  \setcounter{page}{1}
}{}
}

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% Info o dokumentu.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

\AtBeginDocument{
\hypersetup{
  pdftitle={\@title\ (\@thesistype)},
  pdfauthor={\@author\ (\@textsupervisor: \@supervisor)},
  pdfsubject={\@program},
  pdfcreator={\@author},
  pdfkeywords={\@keywords}
}
}

\endinput

# edict-browser #

EdictBrowser is a viewer for EDICT&#169;, a Japanese-English
dictionary.

The dictionary, in its raw form, is a plain text file that you can
read as-is in your browser. This software may make it more convenient
to work with.

## System Requirements ##

1. Java

2. JavaScript

3. Internet browser

## Setup ##

1. Get the files in edict-browser and save them to your hard drive.

2. Get the dictionary from

    <tt>[ftp://ftp.edrdg.org/pub/Nihongo/edict](ftp://ftp.edrdg.org/pub/Nihongo/edict)</tt>

    or

    <tt>[ftp://ftp.edrdg.org/pub/Nihongo/edict.gz](ftp://ftp.edrdg.org/pub/Nihongo/edict.gz)</tt>

    and save it to the same location. Do not change the name or
    encoding of the file. You should have a file named <tt>edict</tt>
    (without a <tt>.txt</tt> extension) in the application directory.
    The dictionary is EUC-JP encoded. If you open <tt>edict</tt> in a
    text editor the first line should look like this ..

    <tt>&#161;&#161;&#161;&#169;&#161;&#169;&#161;&#169; /EDICT, ... </tt>

    The first link above is to a text file that your browser may or
    may not try to open directly. If the Japanese portions render
    correctly it means the browser has deduced the encoding
    automatically. Save it as a text file. If the browser insists on
    giving it a <tt>.txt</tt> extension, rename it to <tt>edict</tt>.
    It is possible the browser will try to save it
    as Unicode text, which you do not want. Set the browser's encoding
    to Western European, ISO-8859-1, or Latin-1 and try again. Or
    right-click on the link and look for something like
    <tt>Save link as ...</tt>.

    The compressed (<tt>.gz</tt>) file may be more reliable. You will
    need software like WinZip or gzip to unpack it.

    And, you can use <tt>ftp</tt> if you like.

    Visit
    <tt>[ftp://ftp.edrdg.org/pub/Nihongo/index.html](ftp://ftp.edrdg.org/pub/Nihongo/index.html)</tt>
    to find links to the dictionary files. Beware that
    <tt>edict2</tt> is similar, but not the file you want.

3. Open the command prompt, change to the application directory, and
   execute the following command.

    <tt>java Setup</tt>

    This creates a file called <tt>edict.js</tt>, which is a copy of
    the dictionary in JavaScript form.

    Or, if you have [Apache Ant](http://ant.apache.org/index.html),
    you can do

    <tt>ant update</tt>

    which will get the dictionary and prepare it.

    <tt>edict</tt> has gotten to be quite a large file. Because this
    application does not use a web server or database, the entire
    dictionary has to be loaded in memory all at once. That can be
    taxing for the system/browser. It may be that edict-browser is
    reaching the limit of what is practical as far as that part of
    the design is concerned. You get simplicity at the expense of
    performance and efficiency. You can use a much smaller subset
    of the full dictionary by using the command

    <tt>java Setup -min</tt>

    which will reduce the size of the dictionary by about 90%. For
    intermediate users that is pretty adequate.

4. Open <tt>index.html</tt> in your browser.

## Usage ##

There is not much to it. You can read one page at a time, or type
something into the input box and hit enter or the spacebar to jump
to wherever you want to go. Click on an entry to save it as a kind
of bookmark. The bookmarks will be saved between sessions only if
the browser has local storage enabled. Likewise, if you delete your
local storage, the bookmarks will be lost.

## Credits ##

Information about EDICT and other related dictionaries can be found
at the

[Electronic Dictionary Research and Development Group](http://www.edrdg.org)

and the

<!--
[EDICT home page](http://www.csse.monash.edu.au/~jwb/edict.html).
-->
[EDICT home page](http://www.edrdg.org/jmdict/edict_doc.html).

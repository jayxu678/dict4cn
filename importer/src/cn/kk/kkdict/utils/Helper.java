package cn.kk.kkdict.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import cn.kk.kkdict.beans.DictRow;
import cn.kk.kkdict.beans.FormattedTreeMap;
import cn.kk.kkdict.beans.FormattedTreeSet;
import cn.kk.kkdict.types.Category;

public final class Helper {
    public static boolean DEBUG = true;
    private static final boolean INFO = true;

    public static final int BUFFER_SIZE = 1024 * 1024 * 4;
    public static final Charset CHARSET_EUCJP = Charset.forName("EUC-JP");
    public static final Charset CHARSET_UTF16LE = Charset.forName("UTF-16LE");
    public static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");
    // public static final String DIR_IN_DICTS = "K:\\kkdict\\in\\dicts";
    // public static final String DIR_IN_WORDS = "K:\\kkdict\\in\\words";
    // public static final String DIR_IN_DICTS = "O:\\kkdict\\in\\dicts";
    // public static final String DIR_IN_WORDS = "O:\\kkdict\\in\\words";
    //
    // public static final String DIR_OUT_DICTS = "O:\\kkdict\\out\\dicts";
    // public static final String DIR_OUT_GENERATED = "O:\\kkdict\\out\\generated";
    // public static final String DIR_OUT_WORDS = "O:\\kkdict\\out\\words";
    public static final String DIR_IN_DICTS = "C:\\usr\\kkdict\\in\\dicts";
    public static final String DIR_IN_WORDS = "C:\\usr\\kkdict\\in\\words";
    public static final String DIR_OUT_DICTS = "C:\\usr\\kkdict\\out\\dicts";
    public static final String DIR_OUT_GENERATED = "C:\\usr\\kkdict\\out\\generated";
    public static final String DIR_OUT_WORDS = "C:\\usr\\kkdict\\out\\words";
    // public static final String DIR_OUT_DICTS = "K:\\kkdict\\out\\dicts";
    // public static final String DIR_OUT_GENERATED = "K:\\kkdict\\out\\generated";
    // public static final String DIR_OUT_WORDS = "K:\\kkdict\\out\\words";

    public final static String EMPTY_STRING = "";
    public final static List<String> EMPTY_STRING_LIST = Collections.emptyList();

    private static final String[][] HTML_ENTITIES = { { "fnof", "402" }, { "Alpha", "913" }, { "Beta", "914" },
            { "Gamma", "915" }, { "Delta", "916" }, { "Epsilon", "917" }, { "Zeta", "918" }, { "Eta", "919" },
            { "Theta", "920" }, { "Iota", "921" }, { "Kappa", "922" }, { "Lambda", "923" }, { "Mu", "924" },
            { "Nu", "925" }, { "Xi", "926" }, { "Omicron", "927" }, { "Pi", "928" }, { "Rho", "929" },
            { "Sigma", "931" }, { "Tau", "932" }, { "Upsilon", "933" }, { "Phi", "934" }, { "Chi", "935" },
            { "Psi", "936" }, { "Omega", "937" }, { "alpha", "945" }, { "beta", "946" }, { "gamma", "947" },
            { "delta", "948" }, { "epsilon", "949" }, { "zeta", "950" }, { "eta", "951" }, { "theta", "952" },
            { "iota", "953" }, { "kappa", "954" }, { "lambda", "955" }, { "mu", "956" }, { "nu", "957" },
            { "xi", "958" }, { "omicron", "959" }, { "pi", "960" }, { "rho", "961" }, { "sigmaf", "962" },
            { "sigma", "963" }, { "tau", "964" }, { "upsilon", "965" }, { "phi", "966" }, { "chi", "967" },
            { "psi", "968" }, { "omega", "969" }, { "thetasym", "977" }, { "upsih", "978" }, { "piv", "982" },
            { "bull", "8226" }, { "hellip", "8230" }, { "prime", "8242" }, { "Prime", "8243" }, { "oline", "8254" },
            { "frasl", "8260" }, { "weierp", "8472" }, { "image", "8465" }, { "real", "8476" }, { "trade", "8482" },
            { "alefsym", "8501" }, { "larr", "8592" }, { "uarr", "8593" }, { "rarr", "8594" }, { "darr", "8595" },
            { "harr", "8596" }, { "crarr", "8629" }, { "lArr", "8656" }, { "uArr", "8657" }, { "rArr", "8658" },
            { "dArr", "8659" }, { "hArr", "8660" }, { "forall", "8704" }, { "part", "8706" }, { "exist", "8707" },
            { "empty", "8709" }, { "nabla", "8711" }, { "isin", "8712" }, { "notin", "8713" }, { "ni", "8715" },
            { "prod", "8719" }, { "sum", "8721" }, { "minus", "8722" }, { "lowast", "8727" }, { "radic", "8730" },
            { "prop", "8733" }, { "infin", "8734" }, { "ang", "8736" }, { "and", "8743" }, { "or", "8744" },
            { "cap", "8745" }, { "cup", "8746" }, { "int", "8747" }, { "there4", "8756" }, { "sim", "8764" },
            { "cong", "8773" }, { "asymp", "8776" }, { "ne", "8800" }, { "equiv", "8801" }, { "le", "8804" },
            { "ge", "8805" }, { "sub", "8834" }, { "sup", "8835" }, { "sube", "8838" }, { "supe", "8839" },
            { "oplus", "8853" }, { "otimes", "8855" }, { "perp", "8869" }, { "sdot", "8901" }, { "lceil", "8968" },
            { "rceil", "8969" }, { "lfloor", "8970" }, { "rfloor", "8971" }, { "lang", "9001" }, { "rang", "9002" },
            { "loz", "9674" }, { "spades", "9824" }, { "clubs", "9827" }, { "hearts", "9829" }, { "diams", "9830" },
            { "OElig", "338" }, { "oelig", "339" }, { "Scaron", "352" }, { "scaron", "353" }, { "Yuml", "376" },
            { "circ", "710" }, { "tilde", "732" }, { "ensp", "8194" }, { "emsp", "8195" }, { "thinsp", "8201" },
            { "zwnj", "8204" }, { "zwj", "8205" }, { "lrm", "8206" }, { "rlm", "8207" }, { "ndash", "8211" },
            { "mdash", "8212" }, { "lsquo", "8216" }, { "rsquo", "8217" }, { "sbquo", "8218" }, { "ldquo", "8220" },
            { "rdquo", "8221" }, { "bdquo", "8222" }, { "dagger", "8224" }, { "Dagger", "8225" }, { "permil", "8240" },
            { "lsaquo", "8249" }, { "rsaquo", "8250" }, { "euro", "8364" }, };
    private static final String[] HTML_KEYS;

    private static final int[] HTML_VALS;
    public final static int MAX_CONNECTIONS = 2;

    public final static String SEP_ATTRIBUTE = "‹";

    public final static String SEP_DEFINITION = "═";

    public final static String SEP_LIST = "▫";

    public final static String SEP_NEWLINE = "\n";

    public final static char SEP_NEWLINE_CHAR = '\n';

    public final static String SEP_PARTS = "║";

    public final static String SEP_PINYIN = "'";

    public static final String SEP_SAME_MEANING = "¶";

    public static final String SEP_SPACE = " ";

    public final static String SEP_WORDS = "│";

    public final static String SEP_ETC = "…";

    public static final byte[] SEP_DEFINITION_BYTES = Helper.SEP_DEFINITION.getBytes(Helper.CHARSET_UTF8);
    public static final byte[] SEP_LIST_BYTES = Helper.SEP_LIST.getBytes(Helper.CHARSET_UTF8);
    public static final byte[] SEP_WORDS_BYTES = Helper.SEP_WORDS.getBytes(Helper.CHARSET_UTF8);
    public static final int[] SEP_WORDS_INTS = ArrayHelper.toIntArray(Helper.SEP_WORDS.getBytes(Helper.CHARSET_UTF8));
    public static final byte[] SEP_ATTRS_BYTES = Helper.SEP_ATTRIBUTE.getBytes(Helper.CHARSET_UTF8);
    public static final byte[] SEP_NEWLINE_BYTES = Helper.SEP_NEWLINE.getBytes(Helper.CHARSET_UTF8);
    public static final byte[] SEP_PARTS_BYTES = Helper.SEP_PARTS.getBytes(Helper.CHARSET_UTF8);
    public static final byte[] SEP_ETC_BYTES = Helper.SEP_ETC.getBytes(Helper.CHARSET_UTF8);
    public static final byte[] SEP_URI_POSTFIX_BYTES = { ':', '/', '/' };
    public static final byte[] SEP_SPACE_BYTES = SEP_SPACE.getBytes(CHARSET_UTF8);
    public static final byte[] SEP_HTML_TAG_START_BYTES = { '&', 'l', 't', ';'};
    public static final byte[] SEP_HTML_TAG_STOP_BYTES = { '&', 'g', 't', ';'};
    static {
        Arrays.sort(HTML_ENTITIES, new Comparator<String[]>() {
            @Override
            public int compare(String[] o1, String[] o2) {
                return o1[0].compareTo(o2[0]);
            }
        });
        HTML_KEYS = new String[HTML_ENTITIES.length];
        HTML_VALS = new int[HTML_ENTITIES.length];
        int i = 0;
        for (String[] pair : HTML_ENTITIES) {
            HTML_KEYS[i] = pair[0];
            HTML_VALS[i] = Integer.parseInt(pair[1]);
            i++;
        }
    }

    public static void add(Map<String, Integer> statMap, String key) {
        Integer counter = statMap.get(key);
        if (counter == null) {
            statMap.put(key, Integer.valueOf(1));
        } else {
            statMap.put(key, Integer.valueOf(counter.intValue() + 1));
        }
    }

    public static String appendCategories(String word, Set<String> categories) {
        if (categories.isEmpty()) {
            return word;
        } else {
            StringBuilder sb = new StringBuilder(word.length() * 2);
            sb.append(word);
            sb.append(Helper.SEP_ATTRIBUTE).append(Category.TYPE_ID);
            for (String c : categories) {
                sb.append(c);
            }
            return sb.toString();
        }
    }

    public static final String appendFileName(String file, String suffix) {
        int indexOf = file.indexOf('.');
        return file.substring(0, indexOf) + suffix + file.substring(indexOf);
    }

    public static final void changeWordLanguage(DictRow word, String currentLng, Map<String, String> translations) {
        if (translations.containsKey(currentLng)) {
            translations.put(currentLng, word.getName());
            String enDef = translations.get(currentLng);
            word.setName(enDef);
            translations.remove(currentLng);
        }
    }

    public static String download(String url) throws IOException {
        return download(url, File.createTempFile("kkdl", null).getAbsolutePath(), true);
    }

    public static String download(String url, String file, boolean overwrite) throws IOException {
        if (DEBUG) {
            System.out.println("下载'" + url + "'到'" + file + "'。。。");
        }
        if (!overwrite && new File(file).exists()) {
            System.err.println("文件'" + file + "'已存在。跳过下载程序。");
            return null;
        } else {
            long start = System.currentTimeMillis();
            URL urlObj = new URL(url);
            URLConnection conn = urlObj.openConnection();
            conn.addRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:10.0.1) Gecko/20100101 Firefox/10.0.1");
            conn.addRequestProperty("Cache-Control", "no-cache");
            conn.addRequestProperty("Pragma", "no-cache");
            conn.setUseCaches(false);
            OutputStream out = null;
            InputStream in = null;
            try {
                out = new BufferedOutputStream(new FileOutputStream(file), BUFFER_SIZE);
                in = new BufferedInputStream(conn.getInputStream(), BUFFER_SIZE);
                writeInputStream(in, out);
            } catch (IOException e) {
                System.err.println("下载失败：" + e.toString());
            } finally {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();

                    long duration = System.currentTimeMillis() - start;
                    if (INFO) {
                        System.out.println("下载文件'" + url + "'（" + formatSpace(new File(file).length()) + "），用时"
                                + Helper.formatDuration(duration) + "（"
                                + Math.round(new File(file).length() / (duration / 1000.0) / 1024.0) + "kbps）。");
                    }
                }
            }
            return file;
        }
    }

    public static ByteBuffer compressFile(String rawFile, int level) throws IOException {
        InputStream in = new FileInputStream(rawFile);
        ByteArrayOutputStream dataOut = new ByteArrayOutputStream(BUFFER_SIZE);
        Deflater def = new Deflater(level);
        OutputStream out = new DeflaterOutputStream(dataOut, def, BUFFER_SIZE);
        writeInputStream(in, out);
        in.close();
        def.end();
        return ByteBuffer.wrap(dataOut.toByteArray());
    }

    public static ByteBuffer compressFile(String rawFile, String dictionaryFile, int level) throws IOException {
        InputStream in = new FileInputStream(rawFile);
        ByteArrayOutputStream dataOut = new ByteArrayOutputStream(BUFFER_SIZE);
        Deflater def = new Deflater(level);
        def.setDictionary(readBytes(dictionaryFile).array());
        OutputStream out = new DeflaterOutputStream(dataOut, def, BUFFER_SIZE);
        writeInputStream(in, out);
        in.close();
        return ByteBuffer.wrap(dataOut.toByteArray());
    }

    public static ByteBuffer decompressFile(String compressedFile) throws IOException {
        InflaterInputStream in = new InflaterInputStream(new FileInputStream(compressedFile));
        ByteArrayOutputStream dataOut = new ByteArrayOutputStream(BUFFER_SIZE);
        writeInputStream(in, dataOut);
        in.close();
        return ByteBuffer.wrap(dataOut.toByteArray());
    }

    public static ByteBuffer decompressFile(String compressedFile, String dictionaryFile) throws IOException {
        Inflater inf = new Inflater();
        InputStream in = new InflaterInputStream(new FileInputStream(compressedFile), inf, BUFFER_SIZE);
        System.out.println(in.read());
        if (inf.needsDictionary()) {
            try {
                inf.setDictionary(readBytes(dictionaryFile).array());
                ByteArrayOutputStream dataOut = new ByteArrayOutputStream(BUFFER_SIZE);
                writeInputStream(in, dataOut);
                in.close();
                return ByteBuffer.wrap(dataOut.toByteArray());
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid dictionary!");
                return null;
            } finally {
                inf.end();
            }
        } else {
            System.err.println("No dictionary needed!");
            inf.end();
            return null;
        }
    }

    /**
     * 
     * @param text
     *            text to analyze
     * @param definitions
     *            array of array of definitions with the first element as its key
     * @return {cutted text, definition key } if found definition otherwise null
     */
    public static String[] findAndCut(String text, String[][] definitions) {
        String def = null;
        int indexOf;
        for (String[] defArray : definitions) {
            for (int g = 1; g < defArray.length; g++) {
                String k = defArray[g];
                if ((indexOf = text.indexOf(k)) != -1) {
                    def = defArray[0];
                    if (indexOf + k.length() == text.length()) {
                        text = text.substring(0, indexOf);
                    } else {
                        text = text.substring(0, indexOf) + text.substring(indexOf + k.length());
                    }
                    break;
                }
            }
        }
        if (def != null) {
            return new String[] { text, def };
        } else {
            return null;
        }
    }

    public final static String formatDuration(final long duration) {
        final long v = Math.abs(duration);
        final long days = v / 1000 / 60 / 60 / 24;
        final long hours = (v / 1000 / 60 / 60) % 24;
        final long mins = (v / 1000 / 60) % 60;
        final long secs = (v / 1000) % 60;
        final long millis = v % 1000;
        StringBuilder out = new StringBuilder();
        if (days > 0) {
            out.append(days).append(':').append(padding(hours, 2, '0')).append(':').append(padding(mins, 2, '0'))
                    .append(":").append(padding(secs, 2, '0')).append(".").append(padding(millis, 3, '0'));
        } else if (hours > 0) {
            out.append(hours).append(':').append(padding(mins, 2, '0')).append(":").append(padding(secs, 2, '0'))
                    .append(".").append(padding(millis, 3, '0'));
        } else if (mins > 0) {
            out.append(mins).append(":").append(padding(secs, 2, '0')).append(".").append(padding(millis, 3, '0'));
        } else {
            out.append(secs).append(".").append(padding(millis, 3, '0'));
        }
        return out.toString();

    }

    public static final String formatSpace(final long limit) {
        if (limit < 1024L) {
            return limit + " B";
        } else if (limit < 1024L * 1024) {
            return Math.round(limit / 10.24) / 100.0 + " KB";
        } else if (limit < 1024L * 1024 * 1024) {
            return Math.round(limit / 1024 / 10.24) / 100.0 + " MB";
        } else if (limit < 1024L * 1024 * 1024 * 1024) {
            return Math.round(limit / 1024.0 / 1024.0 / 10.24) / 100.0 + " GB";
        } else {
            return Math.round(limit / 1024.0 / 1024.0 / 1024.0 / 10.24) / 100.0 + " TB";
        }
    }

    public static boolean isEmptyOrNull(String text) {
        return text == null || text.isEmpty();
    }

    public final static boolean isNotEmptyOrNull(String text) {
        return text != null && text.length() > 0;
    }

    private static boolean isNumber(char c) {
        return c >= '0' && c <= '9';
    }

    public final static String padding(long value, int len, char c) {
        return padding(String.valueOf(value), len, c);
    }

    public final static String padding(String text, int len, char c) {
        if (text != null && len > text.length()) {
            char[] spaces = new char[len - text.length()];
            Arrays.fill(spaces, c);
            return new String(spaces) + text;
        } else {
            return text;
        }
    }

    public static Set<String> parseCategories(File file) {
        String cats = Helper.substringBetween(file.getName(), "(", ")");
        Set<String> categories = new FormattedTreeSet<String>();
        if (Helper.isNotEmptyOrNull(cats)) {
            String[] cs = cats.split(",");
            for (String c : cs) {
                categories.add(c);
            }
        }
        return categories;
    }

    public static String[] parseLanguages(File file) {
        String base = file.getName().substring(0, file.getName().indexOf('.'));
        int idx2 = base.lastIndexOf('_');
        int idx1 = base.lastIndexOf('_', idx2 - 1);
        String lng1 = base.substring(idx1 + 1, idx2);
        String lng2 = base.substring(idx2 + 1);
        return new String[] { lng1, lng2 };
    }

    public final static void precheck(String inFile, String outDirectory) {
        if (!new File(inFile).isFile()) {
            System.err.println("Could not read input file: " + inFile);
            System.exit(-100);
        }
        if (!(new File(outDirectory).isDirectory() || new File(outDirectory).mkdirs())) {
            System.err.println("Could not create output directory: " + outDirectory);
            System.exit(-101);
        }
    }

    public static ByteBuffer readBytes(String file) throws IOException {
        ByteArrayOutputStream dataOut = new ByteArrayOutputStream();
        FileChannel fChannel = new RandomAccessFile(file, "r").getChannel();
        fChannel.transferTo(0, fChannel.size(), Channels.newChannel(dataOut));
        fChannel.close();
        return ByteBuffer.wrap(dataOut.toByteArray());
    }

    public final static Map<String, String> readMapStringString(String text) {
        final String[] many = text.split(Helper.SEP_LIST);
        final Map<String, String> map = new FormattedTreeMap<String, String>();
        for (String d : many) {
            String[] defs = d.split(Helper.SEP_DEFINITION);
            if (defs.length == 2) {
                map.put(defs[0], defs[1]);
            }
        }
        return map;
    }

    public final static DictRow readPinyinWord(String line) {
        if (Helper.isNotEmptyOrNull(line)) {
            String[] parts = line.split(Helper.SEP_PARTS);
            if (parts.length == 2) {
                String name = parts[0];
                String pinyin = parts[1];
                if (Helper.isNotEmptyOrNull(name) && Helper.isNotEmptyOrNull(pinyin)) {
                    DictRow w = new DictRow();
                    w.setName(name);
                    w.setPronounciation(pinyin);
                    return w;
                }
            }
        }
        return null;
    }

    public final static Set<String> readSetString(String text) {
        final String[] many = text.split(Helper.SEP_LIST);
        final Set<String> set = new FormattedTreeSet<String>();
        for (String d : many) {
            set.add(d);
        }
        return set;
    }

    public final static DictRow readWikiWord(String line) {
        if (Helper.isNotEmptyOrNull(line)) {
            final String[] parts = line.split(Helper.SEP_PARTS);
            if (parts.length > 1) {
                final String name = parts[0];
                if (Helper.isNotEmptyOrNull(name)) {
                    final DictRow w = new DictRow();
                    w.setName(name);
                    final Map<String, String> translations = readMapStringString(parts[1]);
                    w.setTranslations(translations);
                    if (parts.length > 2) {
                        Set<String> categories = readSetString(parts[2]);
                        w.setCategories(categories);
                    }
                    return w;
                }
            }
        }
        return null;
    }

    public static String stripCoreText(String line) {
        final int count = line.length();
        StringBuilder sb = new StringBuilder(count);
        int pairIdx = -1;
        char[][] pairs = { { '(', ')' }, { '{', '}' }, { '[', ']' }, { '（', '）' }, { '《', '》' }, { '［', '］' } };

        for (int i = 0; i < count; i++) {
            char c = line.charAt(i);
            if (i == count - 1 && c == ' ') {
                break;
            } else if (c == ' ' && (i + 1 < count && -1 != (pairIdx = ArrayHelper.indexOf(pairs, line.charAt(i + 1))))) {
                i++;
            } else if (-1 != (pairIdx = ArrayHelper.indexOf(pairs, line.charAt(i + 1)))) {
            } else if (-1 != pairIdx && c == pairs[pairIdx][1]) {
                pairIdx = -1;
                if (i + 1 < count && line.charAt(i + 1) == ' ') {
                    i++;
                }
            } else if (pairIdx == -1) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String stripHtmlText(final String line, final boolean startOk) {
        final int count = line.length();
        final StringBuilder sb = new StringBuilder(count);
        boolean ok = startOk;
        char c;
        for (int i = 0; i < count; i++) {
            c = line.charAt(i);
            if (c == '>') {
                ok = true;
            } else if (c == '<') {
                ok = false;
            } else if (ok) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String stripWikiText(final String wiki) {
        final int length = wiki.length();
        // ==title==. remove ''', {}, [], [|(text)], {|}
        final StringBuilder sb = new StringBuilder(length);
        char cp;
        int countEquals = 0;
        int countQuos = 0;
        int countSpaces = 0;
        int linkOpened = -1;
        boolean externalOpened = false;
        char last = '\0';
        for (int i = 0; i < length; i++) {
            cp = wiki.charAt(i);
            if (cp != ' ' && countSpaces > 0) {
                if (last != ' ' && last != '(') {
                    sb.append(' ');
                    last = ' ';
                }
                countSpaces = 0;
            }
            if (cp != '\'' && countQuos == 1) {
                sb.append('\'');
                last = '\'';
                countQuos = 0;
            }
            if (cp != '=' && countEquals == 1) {
                sb.append('=');
                last = '=';
                countEquals = 0;
            }
            switch (cp) {
            case ' ':
                if (externalOpened) {
                    linkOpened = -1;
                }
                countSpaces++;
                continue;
            case '{':
            case '}':
                continue;
            case '[':
                linkOpened = i;
                if (i + 7 < length
                        && ((wiki.charAt(i + 1) == 'h' && wiki.charAt(i + 2) == 't' && wiki.charAt(i + 3) == 't' && wiki
                                .charAt(i + 4) == 'p') || (wiki.charAt(i + 1) == 'f' && wiki.charAt(i + 2) == 't' && wiki
                                .charAt(i + 3) == 'p'))) {
                    externalOpened = true;
                }
                continue;
            case '|':
                linkOpened = -1;
                continue;
            case ']':
                if (linkOpened != -1) {
                    i = linkOpened;
                }
                linkOpened = -1;
                externalOpened = false;
                continue;
            case '\'':
                countQuos++;
                continue;
            case '=':
                countEquals++;
                continue;
            }
            if (linkOpened == -1) {
                sb.append(cp);
                last = cp;
            }
        }
        return sb.toString();
    }

    public static final String substringBetween(final String text, final String start, final String end) {
        return substringBetween(text, start, end, true);
    }

    public static final String substringBetween(final String text, final String start, final String end,
            final boolean trim) {
        final int nStart = text.indexOf(start);
        final int nEnd = text.indexOf(end, nStart + start.length() + 1);
        if (nStart != -1 && nEnd > nStart) {
            if (trim) {
                return text.substring(nStart + start.length(), nEnd).trim();
            } else {
                return text.substring(nStart + start.length(), nEnd);
            }
        } else {
            return null;
        }
    }

    public static String substringBetweenEnclose(String text, String start, String end) {
        final int nStart = text.indexOf(start);
        final int nEnd = text.lastIndexOf(end);
        if (nStart != -1 && nEnd != -1 && nEnd > nStart + start.length()) {
            return text.substring(nStart + start.length(), nEnd);
        } else {
            return null;
        }
    }

    public final static String substringBetweenLast(final String text, final String start, final String end) {
        return substringBetweenLast(text, start, end, true);
    }

    public final static String substringBetweenLast(final String text, final String start, final String end,
            final boolean trim) {
        int nEnd = text.lastIndexOf(end);
        int nStart = -1;
        if (nEnd > 1) {
            nStart = text.lastIndexOf(start, nEnd - 1);
        } else {
            return null;
        }
        if (nStart < nEnd && nStart != -1 && nEnd != -1) {
            if (trim) {
                return text.substring(nStart + start.length(), nEnd).trim();
            } else {
                return text.substring(nStart + start.length(), nEnd);
            }
        } else {
            return null;
        }

    }

    public static String substringBetweenNarrow(String text, String start, String end) {
        final int nEnd = text.indexOf(end);
        int nStart = -1;
        if (nEnd != -1) {
            nStart = text.lastIndexOf(start, nEnd - 1);
        }
        if (nStart < nEnd && nStart != -1 && nEnd != -1) {
            return text.substring(nStart + start.length(), nEnd);
        } else {
            return null;
        }
    }

    public static String toConstantName(String line) {
        final int count = line.length();
        StringBuilder sb = new StringBuilder(count);
        line = line.replace("ß", "_");
        line = line.toUpperCase();
        for (int i = 0; i < count; i++) {
            char c = line.charAt(i);
            if (c == '.' || c == ':' || c == '?' || c == ',' || c == '*' || c == '=' || c == '„' || c == '“'
                    || c == ')' || c == '1' || c == '2' || c == '3' || c == '4') {
                continue;
            } else if (c == ' ' || c == '\'' || c == '-') {
                sb.append('_');
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private static void unescape(Writer writer, String str, int firstAmp) throws IOException {
        writer.write(str, 0, firstAmp);
        int len = str.length();
        for (int i = firstAmp; i < len; i++) {
            char c = str.charAt(i);
            if (c == '&') {
                int nextIdx = i + 1;
                int semiColonIdx = str.indexOf(';', nextIdx);
                if (semiColonIdx == -1) {
                    while (isNumber(str.charAt(i + 1))) {
                        i++;
                    }
                    semiColonIdx = i + 1;
                }
                int amphersandIdx = str.indexOf('&', i + 1);
                if (amphersandIdx != -1 && amphersandIdx < semiColonIdx) {
                    writer.write(c);
                    continue;
                }
                String entityContent = str.substring(nextIdx, semiColonIdx);
                int entityValue = -1;
                int entityContentLen = entityContent.length();
                if (entityContentLen > 0) {
                    if (entityContent.charAt(0) == '#') {
                        if (entityContentLen > 1) {
                            char isHexChar = entityContent.charAt(1);
                            try {
                                switch (isHexChar) {
                                case 'X':
                                case 'x': {
                                    entityValue = Integer.parseInt(entityContent.substring(2), 16);
                                    break;
                                }
                                default: {
                                    entityValue = Integer.parseInt(entityContent.substring(1), 10);
                                }
                                }
                                if (entityValue > 0xFFFF) {
                                    entityValue = -1;
                                }
                            } catch (NumberFormatException e) {
                                entityValue = -1;
                            }
                        }
                    } else {
                        int idx = Arrays.binarySearch(HTML_KEYS, entityContent);
                        if (idx >= 0) {
                            entityValue = HTML_VALS[idx];
                        }
                    }
                } else {
                    writer.write(c);
                    continue;
                }

                if (entityValue > -1) {
                    writer.write(entityValue);
                }
                i = semiColonIdx;
            } else {
                writer.write(c);
            }
        }

    }

    public static String unescapeHtml(String str) {
        try {
            int firstAmp = str.indexOf('&');
            if (firstAmp < 0) {
                return str;
            }

            StringWriter writer = new StringWriter((int) (str.length() * 1.1));
            unescape(writer, str, firstAmp);

            return writer.toString();
        } catch (IOException ioe) {
            return str;
        }
    }

    public static void writeBytes(byte[] data, String file) throws IOException {
        FileOutputStream f = new FileOutputStream(file);
        f.write(data);
        f.close();
    }

    private static void writeInputStream(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        int len;
        while ((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }
    }

    public static final int deleteDirectory(File path) {
        int deleted = 0;
        if (path.exists()) {
            File[] files = path.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleted += deleteDirectory(files[i]);
                } else {
                    if (files[i].delete()) {
                        deleted++;
                    }
                }
            }
        }
        path.delete();
        return deleted;
    }

    public static final String[] getFileNames(final File[] files) {
        String[] filePaths = new String[files.length];
        int i = 0;
        for (File f : files) {
            // System.out.println((i + 1) + ". " + f.getAbsolutePath());
            filePaths[i++] = f.getAbsolutePath();
        }
        return filePaths;
    }

    public final static long getFilesSize(String[] files) {
        long size = 0;
        for (String f : files) {
            size += new File(f).length();
        }
        return size;
    }

    public static String substringAfterLast(String f, String str) {
        int idx = f.lastIndexOf(str);
        if (-1 != idx) {
            return f.substring(idx + str.length());
        }
        return null;
    }

    public static String substringAfter(String f, String str) {
        int idx = f.indexOf(str);
        if (-1 != idx) {
            return f.substring(idx + str.length());
        }
        return null;
    }
}
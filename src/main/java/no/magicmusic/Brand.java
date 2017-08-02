package no.magicmusic;

public class Brand {

    public static final String B1 = String.format("  __  __      _       ____               ____       __  __     _   _   ____                   ____  " /*noparam*/);
    public static final String A = "\\";
    public static final String A2 = "\"";
    public static final String b2 = String.format("U|' %s/ '|uU  /%s%s  uU /%s___|u   ___    U /\"___|    U|' %s/ '|uU |\"|u| | / __\"| u      ___    U /\"___| ", A, A2, A, A2, A);
    public static final String b3 = String.format("%s| |%s/| |/ %s/ _ \\/ \\| |  _ /  |_%s_|   %s| | u      %s| |\\/| |/ \\| |\\| |<\\___ \\/      |_%s_|   %s| | u   ", A, A, A, A2, A, A, A2, A);
    public static final String b4 = String.format(" | |  | |  / ___ %s  | |_| |    | |     | |/__      | |  | |   | |_| | u___) |       | |     | |/__  ", A);
    public static final String b5 = String.format(" |_|  |_| /_/   %s_%s  %s____|  U/| |\\u    \\____|     |_|  |_|  <<\\___/  |____/>>    U/| |\\u    \\____| ", A, A, A);
    public static final String C = "(__)";
    public static final String b6 = String.format("<<,-,,-.   %s%s    >>  _)(|_.-,_|___|_,-._// %s\\     <<,-,,-.  %s )(    )(  %s-,_|___|_,-._// \\\\  ", A, A, A, C, C);
    public static final String b7 = String.format(" (./  %s.) %s  %s%s_)%s_)-' '-(_/%s%s    (./  %s.)     %s  %s     \\_)-' '-(_/%s+ C + ", A, C, C, C, A, C, C, A, C, C, C);

    public static void ing() {
        System.out.printf("%s\n%s\n%s\n%s\n%s\n%s\n%s%n", B1, b2, b3, b4, b5, b6, b7);
    }
}

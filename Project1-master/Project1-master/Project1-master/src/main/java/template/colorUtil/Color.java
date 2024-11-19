package template.colorUtil;

import org.fusesource.jansi.Ansi;
public class Color {
    public static void printRed(String text)
    {
        System.out.println(Ansi.ansi().fgRed().a(text).reset());
    }

    public static void printGreen(String text)
    {
        System.out.println(Ansi.ansi().fgGreen().a(text).reset());
    }

    public static void printYellow(String text)
    {
        System.out.println(Ansi.ansi().fgYellow().a(text).reset());
    }

    
    public static void printBlue(String text)
    {
        System.out.println(Ansi.ansi().fgBlue().a(text).reset());
    }
    public static void printCyan(String text)
    {
        System.out.println(Ansi.ansi().fgCyan().a(text).reset());
    }
    public static void printCyanNo(String text)
    {
        System.out.print(Ansi.ansi().fgCyan().a(text).reset());
    }
}

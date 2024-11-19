package template.colorUtil;

import org.fusesource.jansi.Ansi;
public class Color {
    public static void print_red(String text)
    {
        System.out.println(Ansi.ansi().fgRed().a(text).reset());
    }

    public static void print_green(String text)
    {
        System.out.println(Ansi.ansi().fgGreen().a(text).reset());
    }

    public static void print_yellow(String text)
    {
        System.out.println(Ansi.ansi().fgYellow().a(text).reset());
    }

    public static void print_blue(String text)
    {
        System.out.println(Ansi.ansi().fgBlue().a(text).reset());
    }
}

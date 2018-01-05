package agh.cs.po.szjan.project1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


/**
 * Created by szjan on 20.12.2017.
 */
public class LawReader {
    public static void main(String[] args) {
        try{
            BufferedReader bufferedReader = null;
            FileReader fileReader = null;
            Map<ArgumentLineOptions, String> options = Parser.optionsParser(args);
            String path = options.get(ArgumentLineOptions.File);
            fileReader = new FileReader(path);
            bufferedReader = new BufferedReader(fileReader);
            String line = null;
            String text = "";
            while ((line = bufferedReader.readLine()) != null) {
                text += line;
                text += "\n";
            }
            String preParsed = Parser.preParse(text);
            LawElement spis = Parser.createTableOfContent(preParsed);
            LawElement fullDocument = Parser.parse(preParsed);
            List<LawElement> elementList = new ArrayList<>();
            elementList.add(fullDocument);
            LawElement toPrint = fullDocument;
            if(options.get(ArgumentLineOptions.Mode).equals("1")){
                toPrint = spis;
                if(options.containsKey(ArgumentLineOptions.Section)){
                    toPrint = toPrint.getChild(options.get(ArgumentLineOptions.Section));
                }
                if(options.containsKey(ArgumentLineOptions.Chapter)){
                    toPrint = toPrint.getChild(options.get(ArgumentLineOptions.Chapter));
                }
                System.out.print(toPrint);
            }
            else if(options.get(ArgumentLineOptions.Mode).equals("2")){
                if(options.containsKey(ArgumentLineOptions.Article)){
                    toPrint = toPrint.getChild(options.get(ArgumentLineOptions.Article));
                }
                if(options.containsKey(ArgumentLineOptions.Paragraph)){
                    toPrint = toPrint.getChild(options.get(ArgumentLineOptions.Paragraph));
                }
                if(options.containsKey(ArgumentLineOptions.Point)){
                    toPrint = toPrint.getChild(options.get(ArgumentLineOptions.Point));
                }
                if(options.containsKey(ArgumentLineOptions.Letter)){
                    toPrint = toPrint.getChild(options.get(ArgumentLineOptions.Letter));
                }
                if(options.containsKey(ArgumentLineOptions.RangeOfArticles)){
                    String[] articles = options.get(ArgumentLineOptions.RangeOfArticles).split("-");
                    toPrint = fullDocument.getChild(articles[0]);
                    do{
                        System.out.print(toPrint);
                        toPrint = fullDocument.getChild(fullDocument.getPositionOfChild(toPrint)+1);
                    }while(!toPrint.equals(fullDocument.getChild(articles[1])));
                }
                System.out.print(toPrint);

            }
        } catch (Exception x) {
            x.printStackTrace();
        }

    }
}

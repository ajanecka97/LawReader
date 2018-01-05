package agh.cs.po.szjan.project1;

import java.util.*;


/**
 * Created by szjan on 20.12.2017.
 */
public class Parser {

    public static Map optionsParser(String[] args) throws Exception{
        Map<ArgumentLineOptions,String> options = new HashMap();
        boolean fileIncluded = false;
        boolean modeIncluded = false;
        boolean taskIncluded = false;
        for(int i = 0; i < args.length; i++){
            switch (args[i]){
                case "--File":
                    options.put(ArgumentLineOptions.File, args[i+1]);
                    fileIncluded = true;
                    break;
                case "--Mode":
                    options.put(ArgumentLineOptions.Mode, args[i+1]);
                    modeIncluded = true;
                    break;
                case "--Article":
                    options.put(ArgumentLineOptions.Article, args[i+1]);
                    taskIncluded = true;
                    break;
                case "--Range":
                    options.put(ArgumentLineOptions.RangeOfArticles, args[i+1]);
                    taskIncluded = true;
                    break;
                case "--Paragraph":
                    options.put(ArgumentLineOptions.Paragraph, args[i+1]);
                    taskIncluded = true;
                    break;
                case  "--Point":
                    options.put(ArgumentLineOptions.Point, args[i+1]);
                    taskIncluded = true;
                    break;
                case "--Letter":
                    options.put(ArgumentLineOptions.Letter, args[i+1]);
                    taskIncluded = true;
                    break;
                case "--Section":
                    options.put(ArgumentLineOptions.Section, args[i+1]);
                    taskIncluded = true;
                    break;
                case  "--Chapter":
                    options.put(ArgumentLineOptions.Chapter, args[i+1]);
                    taskIncluded = true;
                    break;
            }
        }
        if(!fileIncluded || !modeIncluded){
            throw new IllegalArgumentException();
        }
        if(!taskIncluded && options.get(ArgumentLineOptions.Mode).equals("2")){
            throw new IllegalArgumentException();
        }
        else{
            return options;
        }
    }

    public static String preParse(String toParse){
        Scanner scanner = new Scanner(toParse);
        String line = null;
        String parsed = "";
        while(scanner.hasNextLine()){
            line = scanner.nextLine();
            if(line.matches(".*-$")){
                line = mergeWords(line, scanner.next());
            }
            if(line.matches("(.Kancelaria Sejmu.*$)|(\\d{4}-\\d{2}-\\d{2})")){
                continue;
            }
            parsed += line;
            parsed += "\n";
        }
        return parsed.replaceAll("(?m)^[ \t]*\r?\n", "").replaceAll("(?m)^[ \t]*", "");
    }

    public static LawElement parse(String toParse){
        Scanner scanner = new Scanner(toParse);
        String line = null;
        LawElement fullDocument = new LawElement(null, null);
        LawElement currentElement = fullDocument;
        Level level = Level.Document;
        while(scanner.hasNextLine()){
            line = scanner.nextLine();
            StringTokenizer st = new StringTokenizer(line);
            if(line.matches("^Art[.] [0-9]+[.].*")){
                while(level != Level.Document){
                    currentElement = currentElement.getParent();
                    level = level.previous();
                }
                String key = st.nextToken() + " " +  st.nextToken();
                LawElement newArticle = new LawElement("", currentElement);
                currentElement.addChild(key, newArticle);
                currentElement = newArticle;
                level = Level.Article;
            }
            if((line.matches("^[0-9][.].*") || line.matches("^Art[.] [0-9]+[.] [0-9]+[.].*"))){
                while(level != Level.Article){
                    currentElement = currentElement.getParent();
                    level = level.previous();
                }
                String key = st.nextToken();
                LawElement newParagraph = new LawElement("", currentElement);
                currentElement.addChild(key, newParagraph);
                currentElement = newParagraph;
                level = Level.Paragraph;
            }
            if(line.matches("^[0-9][)].*")){
                if(level == Level.Article){
                    LawElement newParagraph = new LawElement("", currentElement);
                    currentElement.addChild("" , newParagraph);
                    currentElement = newParagraph;
                    level = Level.Paragraph;
                }
                while(level != Level.Paragraph){
                    currentElement = currentElement.getParent();
                    level = level.previous();
                }
                String key = st.nextToken();
                LawElement newPoint = new LawElement("", currentElement);
                currentElement.addChild(key, newPoint);
                currentElement = newPoint;
                level = Level.Point;
            }
            if(line.matches("^[a-z][)].*")){
                while(level != Level.Point){
                    currentElement = currentElement.getParent();
                    level = level.previous();
                }
                String key = st.nextToken();
                LawElement newLetter = new LawElement("", currentElement);
                currentElement.addChild(key, newLetter);
                currentElement = newLetter;
                level = Level.Letter;
            }
            currentElement.addText("\n");
            currentElement.addText(line);
        }
        return fullDocument;
    }

    public static LawElement createTableOfContent(String text){
        Scanner scanner = new Scanner(text);
        String line = null;
        LawElement tableOfContents = new LawElement("", null);
        LawElement currentElement = tableOfContents;
        TableOfContentsLevels level = TableOfContentsLevels.TableOfContents;
        while(scanner.hasNextLine()){
            line = scanner.nextLine();
            if(line.matches("^DZIAŁ [IVX]+")  || line.matches("^Rozdział [IVX]+")){
                while(level != TableOfContentsLevels.TableOfContents){
                    currentElement = currentElement.getParent();
                    level = level.previous();
                }
                LawElement newSection = new LawElement("", currentElement);
                newSection.addText(line + ": ");
                newSection.addText(scanner.nextLine() + "\n");
                currentElement.addChild(line, newSection);
                currentElement = newSection;
                level = TableOfContentsLevels.Section;
            }
            if(line.matches("^Rozdział [0-9]+")){
                while(level != TableOfContentsLevels.Section){
                    currentElement = currentElement.getParent();
                    level = level.previous();
                }
                LawElement newChapter = new LawElement("", currentElement);
                newChapter.addText(line + ": ");
                newChapter.addText(scanner.nextLine() + "\n");
                currentElement.addChild(line, newChapter);
                currentElement = newChapter;
                level = TableOfContentsLevels.Chapter;
            }
            if(line.matches("^[A-ZĄŁÓŚŻ, ]+$") && level != TableOfContentsLevels.TableOfContents){
                while(level != TableOfContentsLevels.Section){
                    currentElement = currentElement.getParent();
                    level = level.previous();
                }
                LawElement newTitle = new LawElement("", currentElement);
                newTitle.addText(line + "\n");
                currentElement.addChild("", newTitle);
                currentElement = newTitle;
                level = TableOfContentsLevels.Chapter;
            }
        }
        return tableOfContents;
    }

    private static String mergeWords(String line1, String line2){
        line1 = line1.replaceAll("-$", "");
        String merged = line1 + line2;
        return merged;
    }
}

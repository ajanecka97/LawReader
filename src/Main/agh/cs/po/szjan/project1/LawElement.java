package agh.cs.po.szjan.project1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LawElement {
    private String text = null;
    private LawElement parent = null;
    private List<LawElement> lawElementChildrens = new ArrayList<>();
    private Map<String, LawElement> lawElementChildrenMap = new HashMap<>();


    public LawElement(String text, LawElement parent){
        this.text = text;
        this.parent = parent;
    }

    public String toString(){
        String result = text;
        for(LawElement element : lawElementChildrens){
            result += element;
        }
        return result;
    }

    public void addText(String text){
        this.text += text;
    }

    public void addChild(String s, LawElement newChild){
        lawElementChildrens.add(newChild);
        lawElementChildrenMap.put(s, newChild);
    }

    public  LawElement getChild(String s) {
        return lawElementChildrenMap.get(s);
    }
    public LawElement getChild(int i){
        return lawElementChildrens.get(i);
    }

    public int getPositionOfChild(LawElement e){
        return lawElementChildrens.indexOf(e);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LawElement that = (LawElement) o;

        return(text.equals(that.text));
    }

    @Override
    public int hashCode() {
        int result = text.hashCode();
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        result = 31 * result + (lawElementChildrens != null ? lawElementChildrens.hashCode() : 0);
        result = 31 * result + (lawElementChildrenMap != null ? lawElementChildrenMap.hashCode() : 0);
        return result;
    }

    public LawElement getParent(){ return this.parent; }
}

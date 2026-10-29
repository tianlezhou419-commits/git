package com.itheima.domain;

import java.util.ArrayList;

public class HeroCharacter extends Character {
    //角色属性（子类）,技能列表的集合
    public ArrayList<String> skillList;//技能列表


    public HeroCharacter() {
        //空参中顺便把集合的对象也创建了，外界要给我方角色添加技能的时候，无需考虑集合，直接add就行了
        super();
        skillList = new ArrayList<String>();
    }

    public HeroCharacter(String name, int HP, int attack, int defense) {
        super(name, HP, attack, defense);
        skillList = new ArrayList<String>();
    }

    //用来遍历技能列表
    public String showSkillList(){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < skillList.size(); i++){
            sb.append(skillList.get(i));
            if(i != skillList.size() - 1){
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}

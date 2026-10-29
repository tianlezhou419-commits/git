package com.itheima.domain;

//表示敌人游戏人物的角色
public class EnemyCharacter extends Character {

    public String skill;//技能只有一个
    public boolean defending;//是否正在防御

    public EnemyCharacter() {
        super();
    }

    public EnemyCharacter(String name, int HP, int attack, int defense, String skill) {
        super(name, HP, attack, defense);
        this.skill = skill;
    }

    //重写父类方法

    @Override
    public void takeDamage(int damage) {
        //如果敌人正在防御，伤害减半
        if (defending) {
            damage /= 2;
            defending = false;
        }
        //调用父类方法去扣血
        super.takeDamage(damage);

    }
}

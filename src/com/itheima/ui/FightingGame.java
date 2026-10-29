package com.itheima.ui;

import com.itheima.domain.EnemyCharacter;
import com.itheima.domain.HeroCharacter;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class FightingGame {
    //启动游戏
    public void gameStart(String username){
        //1.显示游戏标题
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("    🎮" + username + " 欢迎来到文字格斗游戏 🎮   ");
        System.out.println("╚════════════════════════════════════════╝");

        //2.创建玩家角色(名字+属性)
        HeroCharacter player = createPlayerCharacter(username);

        //3.显示创建角色的信息和技能列表
        System.out.println("角色创建成功！");
        System.out.println("\uD83C\uDF1F初始属性为: " + player.show());
        System.out.println("\uD83C\uDF1F拥有的技能: " + player.showSkillList());

        //4.创建多个敌人列表
        //| 敌人名称 | 生命值 | 攻击力 | 防御力 | 技能（变量）                                           |
        //| -------- | ------ | ------ | ------ | ------------------------------------------------------ |
        //| 初级战士 | 80     | 15     | 10     | 猛击（150%伤害）                                       |
        //| 敏捷刺客 | 60     | 20     | 5      | 快速攻击（2次50%伤害）                                 |
        //| 重装坦克 | 120    | 10     | 20     | 防御姿态（下回合伤害减半） buff（ boolean defendding） |
        //| 神秘法师 | 70     | 25     | 8      | 火球术（180%伤害）
        ArrayList<EnemyCharacter> enemyList = new ArrayList<>();
        enemyList.add(new EnemyCharacter("初级战士", 80, 15, 10, "猛击"));
        enemyList.add(new EnemyCharacter("敏捷刺客", 60, 20, 5, "快速攻击"));
        enemyList.add(new EnemyCharacter("重装坦克", 120, 10, 15, "防御姿态"));
        enemyList.add(new EnemyCharacter("神秘法师", 70, 25, 8, "火球术"));

        //5.准备战斗（依次跟多个敌人战斗）
        int count = 1;//记录当前是第几个敌人战斗
        int win = 0;//记录玩家胜利的次数

        //游戏开始
        while (player.isAlive()){
            // 5.1 重置敌人的属性，敌人属性每场HP+10, ATK+3, DEF+2（敌人：越来越难打）（第二场的时候）
            if(count > 1){
                for(int i=0;i<enemyList.size();i++){
                    EnemyCharacter c = enemyList.get(i);
                    c.maxHP += 10;
                    c.HP = c.maxHP;
                    c.attack += 3;
                    c.defense += 2;
                    c.defending = false;
                }
            }

            //5.2 随机选择敌人(Random)
            Random r = new Random();
            int index = r.nextInt(enemyList.size());
            EnemyCharacter enemy = enemyList.get(index);

            //5.3 战斗开始
            System.out.println("═══════════════════════════════════════");
            System.out.println("⚔\uFE0F 第" + count + "场战斗开始！对手: " + enemy.name);
            System.out.println(enemy.show());

            //跟同一个敌人战斗
            int round = 1;
            while(player.isAlive()){
                System.out.println("--------------------------------------");
                System.out.println("⚔\uFE0F 第" + round + "回合开始！");
                //打印敌我双方的血条
                System.out.println(getHealthBar(player.name,player.HP,player.maxHP));
                System.out.println(getHealthBar(enemy.name,enemy.HP,enemy.maxHP));

                //5.4 玩家回合
                playerTurn(player,enemy);

                //5.5 判断敌人是否被击败
                if(!enemy.isAlive()){
                    //🎉 你击败了 敏捷刺客！
                    System.out.println("\uD83C\uDF89 你击败了" + enemy.name + "！");
                    win++;
                    break;
                }

                //5.6 敌人回合
                enemyTurn(enemy,player);

                //5.7 判断玩家是否被击败
                if(!player.isAlive()){
                    //❌ 你被敌人击败了...
                    System.out.println("\uD83C\uDF89 你被" + enemy.name + "击败了...");
                    break;
                }

                //如果玩家没有被击败，继续下回合
                round++;
            }

            //5.8 战斗结束，判断是否继续跟下一个敌人战斗
            if(player.isAlive()){
                //恢复玩家的血量
                int helHP = r.nextInt(21) + 20; //20-40血量
                player.heal(helHP);

                //💚 战斗结束！你恢复了 36 点生命值
                System.out.println("\uD83D\uDC9A 战斗结束！你恢复了" + helHP + "点生命值");
                //🏆 当前胜场: 1
                System.out.println("\uD83C\uDFC6 当前胜场: " + win);
                System.out.println("═══════════════════════════════════════");
            }

            //5.9 每胜利三场，人物的属性升级
            if(player.isAlive() && win > 0 && win % 3 == 0){
                System.out.println("\uD83C\uDF1F 恭喜！你获得了属性升级！");
                //提升最大生命值
                player.maxHP += 30;
                player.attack += 5;
                player.defense += 3;
                //提示
                System.out.println("最大生命值 +30, 攻击力 +5, 防御力 +3");
                System.out.println("当前属性：" + player.show());
            }

            //5.10 询问玩家是否继续
            if(player.isAlive()){
                System.out.println("是否继续继续战斗？（y/n）");
                Scanner sc = new Scanner(System.in);
                String choose = sc.next();
                if(choose.equalsIgnoreCase("y")){
                    count++;
                    continue;
                }else if(choose.equalsIgnoreCase("n")){
                    break;
                }else {
                    System.out.println("没有这个选项，默认游戏继续");
                    continue;
                }
            }
        }

        //6. 游戏的最终结算
        System.out.println("======================================");
        System.out.println("游戏结束！");
        System.out.println("总胜场：" + win);
        System.out.println("感谢游玩文字版格斗游戏");

        System.exit(0);
    }

    //定义一个方法打印敌我双方的血条
    //满血：zhangsan: [████████████████████] 100/100 HP
    //半血：zhangsan: [██████████          ] 50/100 HP
    //残血：zhangsan: [█                   ] 5/100 HP
    public String getHealthBar(String name,int HP,int maxHP){
        int barLength = 20;//一个方块代表5血量
        int filled = (int)(HP * 1.0 / maxHP * barLength);

        StringBuilder sb = new StringBuilder();
        sb.append(name).append(": [");

        //利用循环拼接方格和空格
        for(int i=0;i<barLength;i++){
            if(i < filled){
                sb.append("█");
            }else{
                sb.append(" ");
            }
        }
        sb.append("] ").append(HP).append("/").append(maxHP).append(" HP");
        return sb.toString();//转回字符串
    }

    //创建一个玩家角色
    public HeroCharacter createPlayerCharacter(String username){
        System.out.println("创建您的角色：");
        System.out.println("您的角色名为" + username);

        //属性分配
        int points = 20;

        //提示
/*        System.out.println("您有" + points + "点属性点可以分配");
        System.out.println("生命值（每点 + 10HP）");
        Scanner sc = new Scanner(System.in);
        int hpPoint = sc.nextInt();

        if (hpPoint < 0){
            System.out.println("无效输入");
            hpPoint = 0;
        }

        if (hpPoint > points){
            System.out.println("您输入的属性点数不能超过" + points);
            hpPoint = points;
        }

        points -= hpPoint;*/

        System.out.println("您有" + points + "点属性点可以分配");
        System.out.println("1.生命值（每点 + 10HP）");
        System.out.println("2.攻击力（每点 + 2ATK）");
        System.out.println("3.防御力（每点 + 1DEF）");

        Scanner sc = new Scanner(System.in);

        //定义数组把要提示的语句存起来
        String[] attributes = {"生命值","攻击力","防御力"};
        //定义数组记录三个属性分配的属性点
        int[] values = new int[3];

        //利用一个循环分配属性点
        for(int i=0;i<attributes.length;i++){
            System.out.println("分配点数到" + attributes[i] + "(剩余" + points + "点)");
            int input = sc.nextInt();

            //如果分配的属性点是负数
            if(input < 0){
                System.out.println("无效输入,默认分配0点");
                input = 0;
            }

            //如果分配的属性点超过剩余属性点
            if(input > points){
                System.out.println("属性点不足！剩余属性点全部分配到" + attributes[i]);
                input = points;
            }

            //计算一下剩余还有多少属性点
            points -= input;

            //记录分配的属性点(最终分配的属性点)
            values[i] = input;
        }

        //创建玩家角色
        HeroCharacter player = new HeroCharacter(username,
                100 + 10*values[0],
                10+ 2*values[1],
                0+ 1*values[2]
        );

        //添加玩家的技能
        player.skillList.add("普通攻击");
        player.skillList.add("强力一击");
        player.skillList.add("生命汲取");

        return player;
    }

    //玩家回合
    public void playerTurn(HeroCharacter player,EnemyCharacter enemy){
        System.out.println("===== 你的回合 =====");
        System.out.println("1. 普通攻击");
        System.out.println("2. 强力一击(消耗10HP)");
        System.out.println("3. 生命汲取 (消耗10HP，恢复生命)");
        System.out.println("请选择您的行动（1 - 3）：");
        Scanner sc = new Scanner(System.in);
        String choose = sc.next();
        switch (choose){
            case "1":
                System.out.println("1. 普通攻击");
                int damage1 = calculateDamage(player.attack,enemy.defense);
                //⚔️ 你对 敏捷刺客 使用了普通攻击，造成 15 点伤害！
                System.out.println("⚔️ 你对 " + enemy.name + " 使用了普通攻击，造成 " + damage1 + " 点伤害！");
                //调用敌人的takeDamage方法，传入伤害值
                enemy.takeDamage(damage1);
                break;
            case "2":
                if(player.HP > 10){
                    System.out.println("2. 强力一击");
                    player.takeDamage(10);
                    int damage2 = calculateDamage((int)(player.attack*1.8),enemy.defense);
                    //💥 消耗10HP，你对 敏捷刺客 使用了强力一击，造成 31 点伤害！
                    System.out.println("💥 消耗10HP，你对 " + enemy.name + " 使用了强力一击，造成 " + damage2 + " 点伤害！");
                    enemy.takeDamage(damage2);
                }else{
                    System.out.println("您的生命值不足，无法使用强力一击！");
                }
                break;
            case "3":
                if(player.HP > 10){
                    System.out.println("3. 生命汲取");
                    player.takeDamage(10);
                    Random r = new Random();
                    int healHP = r.nextInt(21);
                    player.heal(healHP);
                    //💚 消耗10HP,你使用了生命汲取，恢复了 20 点生命值！
                    System.out.println("💚 消耗10HP,你使用了生命汲取，恢复了 " + healHP + " 点生命值！");
                }else{
                    System.out.println("您的生命值不足，无法使用生命汲取！");
                }
                break;
            default:
                System.out.println("没有这个操作，默认普通攻击！");
                break;
        }
    }

    //敌人回合
    private void enemyTurn(EnemyCharacter enemy,HeroCharacter player){
        /*===== 敏捷刺客的回合 =====
          ⚔️ 敏捷刺客 对你使用了普通攻击，造成 15 点伤害！
          ---------------------------------------*/
        System.out.println("===== " + enemy.name + " 的回合 =====");
        String action = "普通攻击";

        //（ 50%的几率普通攻击 / 50%的几率技能攻击 ）
        Random r = new Random();
        int num = r.nextInt(10);

        if(num >= 5){
            action = enemy.skill;
        }

        switch (action){
           /* | 敌人名称 | 生命值 | 攻击力 | 防御力 | 技能（变量）                                           |
| -------- | ------ | ------ | ------ | ------------------------------------------------------ |
| 初级战士 | 80     | 15     | 10     | 猛击（150%伤害）                                       |
| 敏捷刺客 | 60     | 20     | 5      | 快速攻击（2次50%伤害）                                 |
| 重装坦克 | 120    | 10     | 20     | 防御姿态（下回合伤害减半） buff（ boolean defendding） |
| 神秘法师 | 70     | 25     | 8      | 火球术（180%伤害）                                     |
*/
            case "普通攻击":
                System.out.println("敌人采取了普通攻击");
                int damage1 = calculateDamage(enemy.attack,player.defense);
                System.out.println("⚔️ " + enemy.name + " 对你使用了普通攻击，造成 " + damage1 + " 点伤害！");
                player.takeDamage(damage1);
                break;
            case "猛击":
                System.out.println("当前的战士采取了猛击");
                int damage2 = calculateDamage((int)(enemy.attack*1.5),player.defense);
                System.out.println("⚔️ " + enemy.name + " 对你使用了猛击，造成 " + damage2 + " 点伤害！");
                player.takeDamage(damage2);
                break;
            case "快速攻击":
                System.out.println("当前的刺客采取了快速攻击");
                int damage3 = 0;
                for(int i = 0;i<2;i++){
                    damage3 += calculateDamage((int)(enemy.attack*0.5),player.defense);
                }
                System.out.println("⚔️ " + enemy.name + " 对你使用了快速攻击，造成 " + damage3 + " 点伤害！");
                player.takeDamage(damage3);
                break;
            case "防御姿态":
                System.out.println("当前的坦克采取了防御姿态");
                enemy.defending = true;
                System.out.println(enemy.name + " 采取了防御姿态，下回合伤害减半！");
                break;
            case "火球术":
                System.out.println("当前的法师采取了火球术");
                int damage4 = calculateDamage((int)(enemy.attack*1.8),player.defense);
                System.out.println("⚔️ " + enemy.name + " 对你使用了火球术，造成 " + damage4 + " 点伤害！");
                player.takeDamage(damage4);
                break;
        }
    }

    //用来计算伤害(普通攻击)
    public int calculateDamage(int attack,int defense) {
        int damage = attack - defense;
        if(damage < 1){
            damage = 1;
        }
        return damage;
    }

}
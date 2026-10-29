package com.itheima.ui;

import com.itheima.domain.User;

import java.util.ArrayList;
import java.util.Scanner;

public class Login {
    // 开始页面
    public void start() {
        System.out.println("开始页面打开了");

        ArrayList<User> list = new ArrayList<>();

        while (true) {
            System.out.println("╔════════════════════════════════╗");
            System.out.println("    🎮 欢迎来到文字格斗游戏 🎮   ");
            System.out.println("╚════════════════════════════════╝");

            System.out.println("请选择操作：1登录 2注册 3退出");


            Scanner sc = new Scanner(System.in);
            String choose = sc.next();

            switch (choose) {
                case "1" -> login(list);
                case "2" -> register(list);
                case "3" -> {
                    System.out.println("用户选择了退出操作");
                    System.exit(0);
                }
                default -> System.out.println("输入有误，请重新输入");
            }
        }
    }

    // 登录操作
    public void login(ArrayList<User> list) {
        System.out.println("用户选择了登录操作");

        //判断用户名是否存在
        //不存在：提示未注册
        //存在：禁用，提示联系客服
        //存在：验证验证码（用机器直接注册）
        //验证密码是否正确（三次）

        //1.键盘录入用户名
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入用户名：");
        String username = sc.next();

        //2.判断用户名是否存在
        if(!contains(list,username)){
            System.out.println("用户名未注册，请先注册");
            //如果用户名不存在，回到选择页面
            return;
        }

        //3.判断用户是否禁用
        //通过username获取用户对象，再看用户对象的状态是否为禁用
        int index = findIndex(list,username);
        User u = list.get(index);
        if(!u.isStatus()){
            System.out.println("用户已被禁用，请联系客服");
            //如果用户已被禁用，回到选择页面
            return;
        }

        //4.让用户继续录入密码和验证码
        String rightPassword = u.getPassword();
        //验证密码是否正确，最多尝试3次
        for (int i = 0; i < 3; i++) {
            System.out.println("请输入密码");
            String password = sc.next();

            //验证验证码
            while (true) {
                //生成验证码
                String rightcode = getCode();
                System.out.println("验证码为：" + rightcode);
                System.out.println("请输入验证码");
                String code = sc.next();

                if(code.equals(rightcode)){
                    break;
                }else {
                    System.out.println("验证码错误");
                    continue;
                }
            }

            //验证密码
            if(rightPassword.equals(password)){
                System.out.println("登录成功,游戏启动！");
                //创建FightingGame对象，调用游戏方法启动游戏
                FightingGame fg = new FightingGame();
                fg.gameStart(username);
                break;
            }else {
                System.out.println("登陆失败，密码错误");
                if(i == 2) {
                    u.setStatus(false);
                    System.out.println("3次登录均失败，账号已锁定，请联系客服");
                    return;
                }else{
                    System.out.println("密码错误，你还剩下" + (2-i) + "次机会");
                }
            }
        }
    }

    // 注册操作
    public void register(ArrayList<User> list) {
        System.out.println("用户选择了注册操作");
        //注册操作
        //1.创建User对象
        User u = new User();
        //2.键盘录入用户名
        Scanner sc = new Scanner(System.in);

        //ctrl+alt+T选择while循环
        while (true) {
            System.out.println("请输入用户名：");
            String username = sc.next();

            // 1.长度必须在3 ~ 16位
            if (!checkLen(3,16,username)) {
                System.out.println("用户名长度不符合要求，必须在3 ~ 16位");
                continue;
            }

            // 2.只能由字母、数字组成，不能是纯数字
            if(!checkUsername(username)){
                System.out.println("用户名只能由字母、数字组成，不能是纯数字");
                continue;
            }

            // 3.用户名唯一
            // 遍历list，判断用户名是否存在
            if(contains(list,username)){
                System.out.println("用户名已存在，请重新输入");
                continue;
            }

            //当代码执行到这里时，说明用户名符合要求，用户名唯一，用户名不存在，用户名可以注册
            u.setName(username);
            break;
        }

        //3.键盘录入密码
        while (true) {
            System.out.println("请输入密码：");
            String password1 = sc.next();
            System.out.println("确认密码：");
            String password2 = sc.next();

        // 4.密码长度必须在3 ~ 8位
            if(!checkLen(3,8,password1)){
                System.out.println("密码长度不符合要求，必须在3 ~ 8位");
                continue;
            }

            // 只能是字母加数字的组合，不能有其他字母
            // 字母至少有一个
            // 数字至少有一个
            // 其他字符不能有
            if(!checkPassword(password1)){
                System.out.println("密码只能由字母、数字组成，不能是其他字符");
                continue;
            }
            //检验密码是否一致
            if(!password1.equals(password2)){
                System.out.println("两次密码输入不一致，请重新输入");
                continue;
            }
            u.setPassword(password1);
            break;
        }

        //4.把User对象添加到ArrayList中
            list.add(u);

        //5.提示用户注册成功
            System.out.println("注册成功");

    }


    // 判断字符串的长度是否在指定的范围内
    public boolean checkLen(int min, int max, String str) {
        /* if (str.length() < min || str.length() > max) {
            return false;
        } else {
            return true;
        }*/
        return str.length() >= min && str.length() <= max;
    }

    // 判断字符串是否只包含字母、数字,不能是纯数字
    public boolean checkUsername(String username) {
        int letterCount = 0;
        int digitCount = 0;
        int otherCount = 0;

        for (int i = 0; i < username.length(); i++) {
            char c = username.charAt(i);
            if (Character.isLetter(c)) {
                letterCount++;
            } else if (Character.isDigit(c)) {
                digitCount++;
            } else {
                otherCount++;
            }
        }
        return letterCount > 0 && digitCount > 0 && otherCount == 0;
    }

    // 遍历list，判断用户名是否存在
    public boolean contains(ArrayList<User> list, String username){
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getName().equals(username)){
                return true;
            }
        }
        return false;
    }

    // 判断密码是否有数字，字母，其他字符
    public boolean checkPassword(String userInFo){
        int letterCount = 0;
        int digitCount = 0;
        int otherCount = 0;

        for (int i = 0; i < userInFo.length(); i++) {
            char c = userInFo.charAt(i);
            if (Character.isLetter(c)) {
                letterCount++;
            } else if (Character.isDigit(c)) {
                digitCount++;
            } else {
                otherCount++;
            }
        }
        return letterCount > 0 && digitCount > 0 && otherCount == 0;
    }

    // 获取验证码
    public static String getCode() {
        /*验证码规则：
            长度为5
            由4位大写或者小写字母和1位数字组成，同一个字母可重复
            数字可以出现在任意位置*/

        StringBuilder letters = new StringBuilder();
        for (char c = 'a'; c <= 'z'; c++) {
            letters.append(c);
            letters.append((char)(c - 32));
        }

        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int index = (int)(Math.random() * letters.length());
            code.append(letters.charAt(index));
        }
        int digit = (int)(Math.random() * 10);
        int insertPos = (int)(Math.random() * 5);
        code.insert(insertPos, digit);

        return code.toString();
    }

    //在集合中去找username所在的索引
    public int findIndex(ArrayList<User> list, String username){
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getName().equals(username)){
                return i;
            }
        }
        return -1;
    }


}
package org.naiximcn.MiraiCustomHelp;

import net.mamoe.mirai.Bot;

public class Help extends CommandModel{

    public Help() {
        super("help");
    }

    public void onCommand(CommandSender sender, SingleMessage[] args) {
        if(sender instanceof CommandSenderGroup) {
            (CommandSenderGroup) sender.getGroup().sendMessage(quote.plus("---==[ 指令帮助 ]==---\n ◇ /help - 查看指令帮助\n ◇ /mcp 服务器IP - 查询Minecraft服务器信息(仅限Java版)\n ◇ /mcping 服务器IP - 查询Minecraft服务器信息\n ◇ 摸@一个人 - 摸@滴人\n ◇ 摸我 - 摸我自己\n ◇ /mcskin 正版ID - 查询一个正版玩家的皮肤\n ◇ /mcinfo <正版ID> - 查询一个玩家的账号信息\n ◇ /bili-search 类型 内容 - 在Bilibili上查询内容\n ◇ BiliBili视频网址/AvBv号/小程序分享 - 查询该视频信息\n ◇ (/)谁是卧底 子命令 - 奇怪滴小游戏\n天弃之子 - 随机选个笨蛋禁言\n ◇ /psid PixivID - 以ID搜索Pixiv插画\n ◇ /rank 类型 页数 - 获取Pixiv插画排行榜\n◇---==我也是有底线滴 暂时没有辣~==---◇"));
        }
    }
}
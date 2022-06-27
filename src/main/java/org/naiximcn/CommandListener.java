package org.naiximcn.MiraiCustomHelp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.EventChannel;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.event.events.StrangerMessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageSource;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.message.data.SingleMessage;

public class CommandListener extends SimpleListenerHost {
	private String prefix;
	private final Set<CommandModel> commands = new HashSet<CommandModel>();

	/**
	 * <p>
	 * The command listener.
	 * </p>
	 * <p>
	 * You can register it by {@link net.mamoe.mirai.event.EventChannel}
	 * </p>
	 * <p>
	 * For example:
	 * </p>
	 * <p>
	 * {@code GlobalEventChannel.INSTANCE.registerListenerHost(new CommandListener("/"));}
	 * </p>
	 * 
	 * @param prefix the prefix of commands
	 * @author MrXiaoM
	 */
	public CommandListener(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * Use the default prefix "/"
	 * 
	 * @author MrXiaoM
	 */
	public CommandListener() {
		this("/");
	}

	public void setCommandPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getCommandPrefix() {
		return this.prefix;
	}

	public void registerToChannel(EventChannel<Event> channel) {
		channel.registerListenerHost(this);
	}

	public void registerCommand(CommandModel command) {
		this.commands.add(command);
	}

	private void processMessages(MessageEvent event) {
		if (event instanceof StrangerMessageEvent) {
			StrangerMessageEvent sEvent = (StrangerMessageEvent) event;
			this.dispitchCommand(
					new CommandSenderStranger(event.getBot(), sEvent.getSender(), sEvent.getSource(), sEvent.getTime()),
					event.getMessage());
		}
		if (event instanceof FriendMessageEvent) {
			FriendMessageEvent fEvent = (FriendMessageEvent) event;
			this.dispitchCommand(
					new CommandSenderFriend(event.getBot(), fEvent.getSender(), fEvent.getSource(), fEvent.getTime()),
					event.getMessage());
		}
		if (event instanceof GroupMessageEvent) {
			GroupMessageEvent gEvent = (GroupMessageEvent) event;
			this.dispitchCommand(new CommandSenderGroup(event.getBot(), gEvent.getGroup(), gEvent.getSender(),
					gEvent.getSource(), gEvent.getTime()), event.getMessage());
		}
	}

	public void dispitchCommand(CommandSender sender, MessageChain message) {
		String cmdRoot = null;
		List<SingleMessage> args = new ArrayList<SingleMessage>();
		// ������Ϣ�����зֶ�
		int h = 0;
		for (int i = 0; i < message.size(); i++) {
			if (i >= message.size())
				return;
			SingleMessage s = message.get(i);
			// i=0 ʱ��Ϊ MessageSource�������������������棬ֱ������
			if (s instanceof MessageSource) {
				continue;
			}
			// ����ֶ�����ͨ�ı�
			if (s instanceof PlainText) {
				PlainText text = (PlainText) s;
				String str = text.getContent();

				if (h == 0) {
					cmdRoot = str.contains(" ") ? (str.split(" ").length > 0 ? str.split(" ")[0] : null) : str;
					if (cmdRoot == null)
						break;
					// �����������ǰ׺��ͷ�������ֱ�ӽ���
					if (!cmdRoot.startsWith(this.prefix)) {
						cmdRoot = null;
						break;
					}
					// ȥ������ǰ׺
					cmdRoot = cmdRoot.substring(this.prefix.length());
				}
				// ����Ҳ��������ֱ�ӽ���
				if (cmdRoot == null)
					break;
				// �����д��ո����Ϣ�ֶ���Ϊ����
				if (str.contains(" ")) {
					String[] splitText = str.split(" ");
					// ���������ǰ��ķֶΣ���Ҫѡ���1��ʼ���Ǵ�0��ʼ
					// ��1��ʼ�����������Ϊ���������ò�����û�������
					for (int j = (h == 0 ? 1 : 0); j < splitText.length; j++) {
						args.add(new PlainText(splitText[j]));
					}
				} else {
					// û�д��ո�ʱ�ǵ�һ���ֶβż��뵽��������
					// ����ϢΪ���֡�����+@AT+���֡��������ʱ��ᱻ�õ�
					if (h != 1) {
						args.add(text);
					}
				}
				h++;
				// ����д else
				continue;
			}

			// �������������Ϣ�ֶ�����

			// ����ͷ���ķֶε�ʱ���ж������������Ҳ���ֱ����һ������
			if (h > 0 && cmdRoot == null)
				break;

			h++;

			// ������ͨ�ı��ķֶ�ͳһ��������б�
			args.add(s);
		}
		
		if (cmdRoot != null) {
			for (CommandModel model : this.commands) {
				if (cmdRoot.equalsIgnoreCase(model.getCommand())) {
					model.onCommand(sender, args.toArray(new SingleMessage[0]));
				}
			}
		}
	}

	@EventHandler
	private ListeningStatus onGroupMessage(GroupMessageEvent event) {
		this.processMessages(event);
		return ListeningStatus.LISTENING;
	}

	@EventHandler
	private ListeningStatus onFriendMessage(FriendMessageEvent event) {
		this.processMessages(event);
		return ListeningStatus.LISTENING;
	}

	@EventHandler
	private ListeningStatus onStrangerMessage(StrangerMessageEvent event) {
		this.processMessages(event);
		return ListeningStatus.LISTENING;
	}
}

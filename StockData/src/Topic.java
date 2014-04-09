import java.util.Date;


public class Topic {
	String id;
	String user_id;
	String title;
	String created_at;
	int retweet_count;
	int reply_count;
	int fav_count;
	String commentId;
	String text;
	@Override
	public String toString() {
		return "Topic [commentId=" + commentId + ", created_at=" + created_at
				+ ", fav_count=" + fav_count + ", id=" + id + ", reply_count="
				+ reply_count + ", retweet_count=" + retweet_count + ", text="
				+ text + ", title=" + title + ", user_id=" + user_id + "]";
	}
}

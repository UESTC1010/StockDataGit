import java.util.Date;

import com.google.code.morphia.annotations.Id;

public class Topic {
	@Id
	String id;
	String user_id;
	String title;
	Date created_at;
	int retweet_count;
	int reply_count;
	int fav_count;
	String commentId;
	String retweet_status_id;
	String text;
	User user;
	@Override
	public String toString() {
		return "Topic [commentId=" + commentId + ", created_at=" + created_at
				+ ", fav_count=" + fav_count + ", id=" + id + ", reply_count="
				+ reply_count + ", retweet_count=" + retweet_count
				+ ", retweet_status_id=" + retweet_status_id + ", text=" + text
				+ ", title=" + title + ", user=" + user + ", user_id="
				+ user_id + "]";
	}
}

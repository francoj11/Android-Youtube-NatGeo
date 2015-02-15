package francoj11.youtubenatgeo;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 *  Contains information of a youtube video: Title, youtube link and thumbnail link.
 */
public class VideoInfo implements Parcelable{
	private String title;
	private String youtubeLink;
	private String imageLink;

	/**
	 * Construct a VideoInfo object
	 */
	public VideoInfo() {
	
	}

    private VideoInfo(Parcel in){
        title = in.readString();
        youtubeLink = in.readString();
        imageLink = in.readString();
    }
	/**
	 * Get the Video title
	 * @return 		A string containing the video title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Set the video title
	 * @param title		The new video title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Get the youtube link of the video
	 * @return		A string containing the youtube link
	 */
	public String getYoutubeLink() {
		return youtubeLink;
	}

	/**
	 * Set the youtube link
	 * @param youtubeLink	The new youtube link		
	 */
	public void setYoutubeLink(String youtubeLink) {
		this.youtubeLink = youtubeLink;
	}

	/**
	 * Get the image thumbnail link of the video
	 * @return		A string containing the image thumbnail link of the video 
	 */
	public String getImageLink() {
		return imageLink;
	}

	/**
	 * Set the image thumbnail link of the video
	 * @param imageLink		The new image thumbnail link
	 */
	public void setImageLink(String imageLink) {
		this.imageLink = imageLink;
	}


	public String toString(){
		return 	"Title: " + title + " Youtubelink: " + youtubeLink + " ImageLink: " 
				+ imageLink;
	}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(youtubeLink);
        dest.writeString(imageLink);
    }

    public static final Parcelable.Creator<VideoInfo> CREATOR = new Parcelable.Creator<VideoInfo>(){

        @Override
        public VideoInfo createFromParcel(Parcel source) {
            return new VideoInfo(source);
        }

        @Override
        public VideoInfo[] newArray(int size) {
            return new VideoInfo[size];
        }
    };
}

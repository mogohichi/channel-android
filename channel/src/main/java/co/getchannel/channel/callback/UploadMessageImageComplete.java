package co.getchannel.channel.callback;

import co.getchannel.channel.responses.CHMessageImageResponse;

/**
 * Created by rataphon on 9/4/2017 AD.
 */

public interface UploadMessageImageComplete {
    public void complete(CHMessageImageResponse data);
}

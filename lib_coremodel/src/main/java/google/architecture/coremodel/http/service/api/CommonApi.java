package google.architecture.coremodel.http.service.api;

import google.architecture.coremodel.datamodel.http.entities.GirlsData;
import google.architecture.coremodel.http.result.base.Response;
import google.architecture.coremodel.http.service.config.APIUrl;
import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @author zhanglianglin
 * @version 1.0
 * @desc
 * @since 2018/09/03
 */

public interface CommonApi {

    @GET(APIUrl.GET_GIRLS_DATAS)
    Flowable<Response<GirlsData>> getGirlsDatas(@Path("size") String size, @Path("index") String index);
}

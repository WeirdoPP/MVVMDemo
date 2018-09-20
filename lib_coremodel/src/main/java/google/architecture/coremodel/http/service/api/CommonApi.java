package google.architecture.coremodel.http.service.api;

import google.architecture.coremodel.datamodel.http.entities.GirlsData;
import google.architecture.coremodel.http.result.base.Response;
import google.architecture.coremodel.http.service.config.APIUrl;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @author zhanglianglin
 * @version 1.0
 * @desc
 * @since 2018/09/03
 */

public interface CommonApi {

    /**
     * max-age 的值单位是秒
     *
     * @param size
     * @param index
     * @return
     */
//    @Headers({CacheInterceptor.HEADER_DYNAMIC_CONDITION_GET
//            , "Cache-Control: max-age=10"
//    })
    @GET(APIUrl.GET_GIRLS_DATAS)
    Observable<Response<GirlsData>> getGirlsDatas(@Path("size") String size, @Path("index") String index);
}

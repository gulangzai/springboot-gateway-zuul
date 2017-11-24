package com.jiujichina;

import java.util.HashMap; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.google.common.collect.Maps;
import com.jiujichina.model.TbUser;

import rx.Observable;
import rx.Observer;
 

@RestController
public class AggregationController {

	public static final com.alibaba.dubbo.common.logger.Logger LOGGER = LoggerFactory.getLogger(AggregationController.class);
	
	@Autowired
	private AggregationService aggregationService;
	
	public DeferredResult<HashMap<String,TbUser>> aggregate(@PathVariable String id){
		Observable<HashMap<String,TbUser>> result = this.aggregateObservable(id);
		return this.toDeferredResult(result);
	}
	
	public Observable<HashMap<String,TbUser>> aggregateObservable(String id){
		return Observable.zip(
		   this.aggregationService.getUserById(id),
		   this.aggregationService.getMovieUserByUserId(id),
		   (user,movieUser)->{
			  HashMap<String,TbUser> map = Maps.newHashMap();
			  map.put("user", user);
			  map.put("movieUser", movieUser);
			  return map;
		   }
	   );
	}
	
    public DeferredResult<HashMap<String,TbUser>> toDeferredResult(Observable<HashMap<String,TbUser>> details){
    	DeferredResult<HashMap<String,TbUser>> result = new DeferredResult<>();
    	details.subscribe(new Observer<HashMap<String,TbUser>>(){
    		@Override
    		public void onCompleted(){
    			LOGGER.info("完成....");
    		}
    		 
    		@Override
    		public void onNext(HashMap<String,TbUser> movieDetails){
    			result.setResult(movieDetails);
    		}

			@Override
			public void onError(Throwable arg0) {
				// TODO Auto-generated method stub
				
			}
    		
    	
    	});
    	return result;
    }
}

package com.jiujichina;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.jiujichina.model.TbUser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import rx.Observable;

@Service
public class AggregationService {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@HystrixCommand(fallbackMethod="fallback")
	public Observable<TbUser> getUserById(String id){
		return Observable.create(observer ->{
			TbUser tbUser  = restTemplate.getForObject("http://springboot-provider-user/{id}",TbUser.class,id);
		    observer.onNext(tbUser);
		    observer.onCompleted();
		});
	}
	
	@HystrixCommand(fallbackMethod="fallback")
	public Observable<TbUser> getMovieUserByUserId(String id){
		return Observable.create(observer ->{
			TbUser tbUser  = restTemplate.getForObject("http://springboot-consume-user/{id}",TbUser.class,id);
		    observer.onNext(tbUser);
		    observer.onCompleted();
		});
	}
	
	public TbUser fallback(String id){
		TbUser tbUser = new TbUser();
		tbUser.setName("默认用户");
		return tbUser;
	}
	
}

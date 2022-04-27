package com.hyk.androidnote.rxjava;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Nullable;

import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableEmitter;
import io.reactivex.rxjava3.core.FlowableOnSubscribe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.functions.BiFunction;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * 创建日期：2022/4/27 on 16:33
 * 描述:
 * 作者:hyk
 */
public class RxjavaStudy extends Activity {

    private static final String TAG = "RxjavaStudy";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Observable.just("123").subscribe(new Consumer<String>() {
//            @Override
//            public void accept(String s) throws Exception {
//                Log.d("yink", "accept ..." + s);
//            }
//        });
//
//        Test();
//        test2();

        Observable observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                Log.d(TAG, "1");
                e.onNext(1);
                SystemClock.sleep(2000);
                Log.d(TAG, "2");
                e.onNext(2);
                SystemClock.sleep(5000);
                Log.d(TAG, "3");
                e.onNext(3);
                SystemClock.sleep(1000);
                Log.d(TAG, "4");
                e.onNext(4);
                SystemClock.sleep(1000);
                Log.d(TAG, "onComplete");
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io());

        Observable observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                Log.d(TAG, "A");
                e.onNext("A");
                SystemClock.sleep(1000);
                Log.d(TAG, "B");
                e.onNext("B");
                SystemClock.sleep(1000);
                Log.d(TAG, "C");
                e.onNext("C");
                SystemClock.sleep(1000);
                Log.d(TAG, "onComplete");
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io());

        Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer a, String b) throws Throwable {
                return a+b;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String o) throws Throwable {
                Log.i("show--->", o);
            }
        });
    }

    private void test2() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
            }
        })/*.map(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) throws Exception {
                return "map add " + integer;
            }
        })*//*.as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer s) throws Exception {
                        Log.d("yink", s+"<<<");
                    }
                });*/

                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Throwable {

                    }
                });
    }

    private void Test() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                for (int j = 0; j <= 100; j++) {
                    e.onNext(j);
                    Log.i("yink", " send id = " + j);
                    try {
                        Thread.sleep(50);
                    } catch (Exception ex) {
                    }
                }
            }
        }, BackpressureStrategy.MISSING)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE); //观察者设置接收事件的数量,如果不设置接收不到事件
                    }

                    @Override
                    public void onNext(Integer integer) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.e("yink", "onNext = " + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.e("yink", "onError = " + t.toString());
                    }

                    @Override
                    public void onComplete() {
                        Log.e("yink", "onComplete");
                    }
                });
    }


}

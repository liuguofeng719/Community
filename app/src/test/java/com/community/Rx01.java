package com.community;

/**
 * Created by liuguofeng719 on 2016/8/10.
 * rx创建操作符
 */
public class Rx01 {

//    /**
//     * create 创建
//     *
//     * @throws Exception
//     */
//    @Test
//    public void rxCreate() throws Exception {
//        //发布服务
//        Observable.create(new Observable.OnSubscribe<Integer>() {
//            @Override
//            public void call(Subscriber<? super Integer> subscriber) {
//                try {
//                    if (!subscriber.isUnsubscribed()) {
//                        for (int i = 0; i < 5; i++) {
//                            subscriber.onNext(i);
//                        }
//                    }
//                    subscriber.onCompleted();
//                } catch (Exception e) {
//                    subscriber.onError(e.fillInStackTrace());
//                }
//
//            }
//        }).subscribe(new Subscriber<Integer>() {//订阅服务
//            @Override
//            public void onCompleted() {
//                System.out.println("sequence completed");
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                System.out.println(e.getMessage());
//            }
//
//            @Override
//            public void onNext(Integer integer) {
//                System.out.println(integer);
//            }
//        });
//    }
//
//    /**
//     * from创建符
//     *
//     * @throws Exception
//     */
//    @Test
//    public void rxFrom() throws Exception {
//
//        Integer[] from = {1, 2, 3, 4, 5};
//
//        Observable.from(from).subscribe(new Action1<Integer>() {
//            @Override
//            public void call(Integer integer) {
//                System.out.println(integer);
//            }
//        }, new Action1<Throwable>() {
//            @Override
//            public void call(Throwable throwable) {
//                System.out.println("throwable");
//            }
//        }, new Action0() {
//            @Override
//            public void call() {
//                System.out.println("call");
//            }
//        });
//    }
//
//    /**
//     * just 创建符
//     *
//     * @throws Exception
//     */
//    @Test
//    public void onJust() throws Exception {
//        Integer[] just = {1, 2, 3, 4, 5, 6};
//        Observable.just(just).subscribe(new Subscriber<Integer[]>() {
//            @Override
//            public void onCompleted() {
//                System.out.println("completed");
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                System.out.println("onError");
//            }
//
//            @Override
//            public void onNext(Integer[] integers) {
//                for (int i = 0; i < integers.length; i++) {
//                    System.out.println("onNext" + integers[i]);
//                }
//            }
//        });
//    }
//
//    /**
//     * timer 定时器，延迟多久执行
//     *
//     * @throws Exception
//     */
//    @Test
//    public void onTimer() throws Exception {
//        Observable.timer(2, TimeUnit.SECONDS).subscribe(new Subscriber<Long>() {
//            @Override
//            public void onCompleted() {
//                System.out.println("onCompleted");
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                System.out.println("onError");
//            }
//
//            @Override
//            public void onNext(Long aLong) {
//                System.out.println("onNext" + aLong.toString());
//            }
//        });
//    }
//
//    /**
//     * InterVal 每隔多少久执行一次
//     *
//     * @throws Exception
//     */
//    @Test
//    public void onInterVal() throws Exception {
//        Observable.interval(2, TimeUnit.SECONDS).subscribe(new Subscriber<Long>() {
//            @Override
//            public void onCompleted() {
//                System.out.println("onCompleted");
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                System.out.println("onError");
//            }
//
//            @Override
//            public void onNext(Long aLong) {
//                System.out.println("onNext" + aLong.toString());
//            }
//        });
//    }
//
//    /**
//     * Range 每隔多少久执行一次
//     *
//     * @throws Exception
//     */
//    @Test
//    public void onRange() throws Exception {
//        Observable.range(1, 10).subscribe(new Subscriber<Integer>() {
//            @Override
//            public void onCompleted() {
//                System.out.println("onCompleted");
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                System.out.println("onError");
//            }
//
//            @Override
//            public void onNext(Integer aLong) {
//                System.out.println("onNext" + aLong);
//            }
//        });
//    }
//    /**
//     * onRepeat 重复执行机器
//     *
//     * @throws Exception
//     */
//    @Test
//    public void onRepeat() throws Exception {
//        Observable.range(1, 5).repeat(2).subscribe(new Subscriber<Integer>() {
//            @Override
//            public void onCompleted() {
//                System.out.println("onCompleted");
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                System.out.println("onError");
//            }
//
//            @Override
//            public void onNext(Integer aLong) {
//                System.out.println("onNext" + aLong);
//            }
//        });
//    }
//    /**
//     * onRepeatWhen 重复执行机器
//     *
//     * @throws Exception
//     */
//    @Test
//    public void onRepeatWhen() throws Exception {
//        Observable.range(1, 5).repeat(2).subscribe(new Subscriber<Integer>() {
//            @Override
//            public void onCompleted() {
//                System.out.println("onCompleted");
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                System.out.println("onError");
//            }
//
//            @Override
//            public void onNext(Integer aLong) {
//                System.out.println("onNext" + aLong);
//            }
//        });
//    }
}

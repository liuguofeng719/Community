package com.community;

/**
 * Created by liuguofeng719 on 2016/8/10.
 * rx转换操作符
 */
public class Rx02 {

//    /**
//     * Buffer 重复执行机器
//     *
//     * @throws Exception
//     */
//    @Test
//    public void onBuffer() throws Exception {
//        //定义邮件内容
//        final String[] mails = new String[]{"Here is an email!", "Another email!", "Yet another email!"};
//        //每隔1秒就随机发布一封邮件
//        Observable<String> endlessMail = Observable.create(new Observable.OnSubscribe<String>() {
//            @Override
//            public void call(Subscriber<? super String> subscriber) {
//                try {
//                    if (subscriber.isUnsubscribed()) return;
//                    Random random = new Random();
//                    while (true) {
//                        String mail = mails[random.nextInt(mails.length)];
//                        subscriber.onNext(mail);
//                        Thread.sleep(1000);
//                    }
//                } catch (Exception ex) {
//                    subscriber.onError(ex);
//                }
//            }
//        }).subscribeOn(Schedulers.io());
//        //把上面产生的邮件内容缓存到列表中，并每隔3秒通知订阅者
//        endlessMail.buffer(3, TimeUnit.SECONDS).subscribe(new Action1<List<String>>() {
//            @Override
//            public void call(List<String> list) {
//
//                System.out.println(String.format("You've got %d new messages!  Here they are!", list.size()));
//                for (int i = 0; i < list.size(); i++)
//                    System.out.println("**" + list.get(i).toString());
//            }
//        });
//    }
//
//    private Observable<File> listFiles(final File f) {
//        if (f.isDirectory()) {
//            return Observable.from(f.listFiles()).flatMap(new Func1<File, Observable<File>>() {
//                @Override
//                public Observable<File> call(File file) {
//                    return listFiles(f);
//                }
//            });
//        } else {
//            return Observable.just(f);
//        }
//    }
//
//    @Test
//    public void fitMap() throws Exception {
//        Observable.just(new File("D:\\document\\export\\"))
//                .flatMap(new Func1<File, Observable<File>>() {
//                    @Override
//                    public Observable<File> call(File file) {
//                        //参数file是just操作符产生的结果，这里判断file是不是目录文件，如果是目录文件，则递归查找其子文件flatMap操作符神奇的地方在于，
//                        // 返回的结果还是一个Observable，而这个Observable其实是包含多个文件的Observable的，输出应该是ExternalCacheDir下的所有文件
//                        return listFiles(file);
//                    }
//                })
//                .subscribe(new Action1<File>() {
//                    @Override
//                    public void call(File file) {
//                        System.out.println(file.getAbsolutePath());
//                    }
//                });
//    }
}

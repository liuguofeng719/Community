package com.joinsmile.community.api;

import com.joinsmile.community.bean.AlipayVo;
import com.joinsmile.community.bean.AnnouncementResp;
import com.joinsmile.community.bean.AnnouncementVo;
import com.joinsmile.community.bean.AnnouncementsResp;
import com.joinsmile.community.bean.ApartmentNumbersResp;
import com.joinsmile.community.bean.ApartmentNumbersVo;
import com.joinsmile.community.bean.ApartmentOwnerPhoneNumberVo;
import com.joinsmile.community.bean.ApartmentPropertyCostResp;
import com.joinsmile.community.bean.AuthticationVo;
import com.joinsmile.community.bean.BaseInfoVo;
import com.joinsmile.community.bean.BuildingUnitsResp;
import com.joinsmile.community.bean.BuildingUnitsVo;
import com.joinsmile.community.bean.CataloguesVo;
import com.joinsmile.community.bean.CityInfoResp;
import com.joinsmile.community.bean.CityListResp;
import com.joinsmile.community.bean.CityVo;
import com.joinsmile.community.bean.InvestigationAnswerVo;
import com.joinsmile.community.bean.InvestigationListResp;
import com.joinsmile.community.bean.InvestigationQuestionListResp;
import com.joinsmile.community.bean.InvestigationQuestionVo;
import com.joinsmile.community.bean.InvestigationStatisticsResp;
import com.joinsmile.community.bean.InvestigationVo;
import com.joinsmile.community.bean.MessageInviteVo;
import com.joinsmile.community.bean.MessageVo;
import com.joinsmile.community.bean.OnTopProductListResp;
import com.joinsmile.community.bean.OpenDoor;
import com.joinsmile.community.bean.OrderInfoResp;
import com.joinsmile.community.bean.PicturesVo;
import com.joinsmile.community.bean.PicturesVoResp;
import com.joinsmile.community.bean.ProductListResp;
import com.joinsmile.community.bean.ProductOrderListResp;
import com.joinsmile.community.bean.ProductOrderVo;
import com.joinsmile.community.bean.ProductOrderVoResp;
import com.joinsmile.community.bean.ProductResp;
import com.joinsmile.community.bean.ProductVo;
import com.joinsmile.community.bean.ProvinceListResp;
import com.joinsmile.community.bean.ProvinceVo;
import com.joinsmile.community.bean.ReceiveProductAddressResp;
import com.joinsmile.community.bean.ReceiveProductAddressVo;
import com.joinsmile.community.bean.ReceiverAddressVo;
import com.joinsmile.community.bean.RecommendProductListResp;
import com.joinsmile.community.bean.RecommendProductVo;
import com.joinsmile.community.bean.RepairAndComplaintDetailVo;
import com.joinsmile.community.bean.RepairAndComplaintResp;
import com.joinsmile.community.bean.RepairAndComplaintsResp;
import com.joinsmile.community.bean.RepairAndComplaintsVo;
import com.joinsmile.community.bean.ResidentialBuildingVo;
import com.joinsmile.community.bean.ResidentialListResp;
import com.joinsmile.community.bean.ServiceCompanyDetail;
import com.joinsmile.community.bean.ServiceCompanyVo;
import com.joinsmile.community.bean.ServiceOrderVo;
import com.joinsmile.community.bean.ShoppingCartResp;
import com.joinsmile.community.bean.ShoppingCartVo;
import com.joinsmile.community.bean.SubCatalogues;
import com.joinsmile.community.bean.UserApartmentPropertyOrdersResp;
import com.joinsmile.community.bean.UserApartmentPropertyOrdersVo;
import com.joinsmile.community.bean.UserInvestigationListResp;
import com.joinsmile.community.bean.UserVoResp;
import com.joinsmile.community.bean.VersionVo;
import com.joinsmile.community.bean.WXPayVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

/**
 * Created by liuguofeng719 on 2016/7/9.
 */
public interface ApisNew {

    //社区系统BaseUri
    String BASE_URI = "http://www.troycd.com:9017/";

    /**
     * 获取版本信息
     *
     * @return
     */
    @GET("common/CheckVersion.ashx?platform=android")
    Call<VersionVo> getVersion();

    /**
     * 获取轮播图接口
     */
    @GET("Advertisement/GetAdvertisementPictures.ashx?platform=android")
    Call<PicturesVoResp<List<PicturesVo>>> getAdvertisementPictures();

    /**
     * 获取所有城市
     */
    @GET("Basic/GetCities.ashx")
    Call<CityListResp<List<CityVo>>> getCities();

    /**
     * 获取所有的省份
     */
    @GET("Basic/GetAllProvinces.ashx")
    Call<ProvinceListResp<List<ProvinceVo>>> getAllProvinces();

    /**
     * 获取省份下所有城市
     *
     * @param provinceID
     */
    @GET("Basic/GetAdministrativeCities.ashx")
    Call<CityListResp<List<CityVo>>> getAdministrativeCities(@Query("provinceID") String provinceID);

    /**
     * 通过gps 获取城市
     *
     * @return
     */
    @GET("GPS/GetGPSLocationCity.ashx")
    Call<CityInfoResp<CityVo>> getGPSLocationCity();


    /**
     * 通过城市编码，获取城市下面的小区
     *
     * @param cityID
     * @return
     */
    @GET("Basic/GetResidentialBuildings.ashx")
    Call<ResidentialListResp<List<ResidentialBuildingVo>>> getResidentialBuildings(
            @Query("cityID") String cityID
    );

    /**
     * 通过小区编码，获取小区下的所有单元
     *
     * @param buildingID
     * @return
     */
    @GET("Basic/GetBuildingUnits.ashx")
    Call<BuildingUnitsResp<List<BuildingUnitsVo>>> getBuildingUnits(
            @Query("buildingID") String buildingID
    );

    /**
     * 小区单元获取对应的门牌号
     *
     * @param unitID
     * @return
     */
    @GET("Basic/GetApartmentNumbers.ashx")
    Call<ApartmentNumbersResp<List<ApartmentNumbersVo>>> getApartmentNumbers(
            @Query("unitID") String unitID
    );

    /**
     * 门牌绑定的业主手机号
     *
     * @param numberID
     * @return
     */
    @GET("Basic/GetApartmentOwnerPhoneNumber.ashx")
    Call<ApartmentOwnerPhoneNumberVo> getApartmentOwnerPhoneNumber(
            @Query("numberID") String numberID
    );

    /**
     * 我的小区
     *
     * @param userID 用户编码
     * @return
     */
    @GET("Users/GetUserApartments.ashx")
    Call<ApartmentNumbersResp<List<ApartmentNumbersVo>>> getUserApartments(
            @Query("userID") String userID
    );

    /**
     * 设置默认小区
     *
     * @param userID   用户编码
     * @param numberID 门牌号
     * @return
     */
    @GET("Users/SetUserDefaultApartments.ashx")
    Call<BaseInfoVo> setUserDefaultApartments(
            @Query("userID") String userID,
            @Query("numberID") String numberID
    );

    /**
     * 注册账号
     *
     * @param mobileNumber
     * @param verifyCode
     * @param password
     * @return {"IsSuccessfully":true,"ErrorMessage":""}
     */
    @GET("Users/Register.ashx")
    Call<BaseInfoVo> register(
            @Query("mobileNumber") String mobileNumber,
            @Query("password") String password,
            @Query("verifyCode") String verifyCode
    );

    /**
     * 短信接口
     *
     * @param mobileNumber
     * @return {"VerifyCode":"652821","IsSuccessfully":true,"ErrorMessage":""}
     */
    @GET("SMS/SendVerifyCode.ashx")
    Call<MessageVo> getVerifyCode(
            @Query("mobileNumber") String mobileNumber
    );

    /**
     * 检验手机是否注册,注册发短信
     *
     * @param mobileNumber
     * @return
     */
    @GET("SMS/SendInvitedCode.ashx")
    Call<MessageInviteVo> sendInvitedCode(
            @Query("mobileNumber") String mobileNumber
    );

    /**
     * 邀请绑定房屋
     *
     * @param userID
     * @param numberID 房间号
     * @param userRole 用户角色 （1: 业主本人 2: 代理业主 3: 家庭成员）
     * @return
     */
    @GET("Basic/BindingHouse.ashx")
    Call<BaseInfoVo> bindingHouse(
            @Query("userID") String userID,
            @Query("numberID") String numberID,
            @Query("userRole") int userRole
    );

    /**
     * 登陆接口
     *
     * @param phoneNumber
     * @param password
     * @return
     */
    @GET("Users/Authtication.ashx")
    Call<AuthticationVo> authentication(
            @Query("phoneNumber") String phoneNumber,
            @Query("password") String password
    );

    /**
     * 获取用户信息
     *
     * @param userID
     * @return
     */
    @GET("Users/GetUserInfo.ashx")
    Call<UserVoResp> getUserInfo(
            @Query("userID") String userID
    );


    /**
     * 修改用户信息
     *
     * @param userID   用户编码
     * @param nickName 昵称
     * @param sex      性别
     * @param file     头像
     * @return
     */
    @Multipart
    @POST("Users/ModifyUserInfo.ashx")
    Call<BaseInfoVo> modifyUserInfo(
            @PartMap HashMap<String, RequestBody> bodyHashMap
    );

    /**
     * 重置密码
     *
     * @param mobileNumber 注册手机号
     * @param newPassword  修改后的新密码
     * @param verifyCode   发送到手机端的验证码
     * @return
     */
    @GET("Users/ResetPassword.ashx")
    Call<BaseInfoVo> resetPassword(
            @Query("mobileNumber") String mobileNumber,
            @Query("newPassword") String newPassword,
            @Query("verifyCode") String verifyCode
    );

    /**
     * 获取用户自己的报修或投诉
     *
     * @param userID
     * @param isRepair 是否是保修和投诉
     * @return
     */
    @GET("RepairAndComplaints/GetRepairAndComplaints.ashx")
    Call<RepairAndComplaintsResp<List<RepairAndComplaintsVo>>> getRepairAndComplaints(
            @Query("userID") String userID,
            @Query("isRepair") int isRepair,
            @Query("isFinished") int isFinished
    );

    /**
     * 获取报修和投诉的详情
     *
     * @param complaintID
     * @return
     */
    @GET("RepairAndComplaints/GetRepairAndComplaintsDetails.ashx")
    Call<RepairAndComplaintResp<RepairAndComplaintDetailVo>> getRepairAndComplaintsDetails(
            @Query("complaintID") String complaintID
    );

    /**
     * 获取用户自己的报修或投诉  上传多张图片
     * userID 用户ID
     * apartmentNumberID门牌ID
     * title标题
     * description描述
     * linkmanPhoneNumber 联系人电话
     * isRepair 是否为报修
     * p1 现场图1 p2 现场图2
     * p3 现场图3 p4 现场图4 p5 现场图5
     *
     * @return RequestBody.create(MediaType.parse("multipart/form-data"), file);
     * Map<String, RequestBody> map = new HashMap<>();
     * map.put("Id", AZUtils.toRequestBody(eventId));
     * map.put("Name", AZUtils.toRequestBody(titleView.getValue()));
     * <p/>
     * if (imageUri != null) {
     * File file = new File(imageUri.getPath());
     * image/*
     * RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), file);
     * map.put("file\"; filename=\"pp.png\"", fileBody);
     * }
     * public static RequestBody toRequestBody (String value) {
     * RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
     * return body ;
     * }
     */
    @Multipart
    @POST("RepairAndComplaints/AddRepairAndComplaints.ashx")
    Call<RepairAndComplaintsResp<RepairAndComplaintsVo>> addRepairAndComplaints(
            @PartMap Map<String, RequestBody> requestBodyMap
    );

    /**
     * 上传单张图片
     *
     * @param photo
     * @return
     */
    @Multipart
    @POST("Picture/UploadPicture.ashx")
    Call<BaseInfoVo> uploadPicture(@Part("file\"; filename=\"image.png\"") RequestBody photo);

    /**
     * 拉取最新的一条公告信息
     *
     * @param buildingID 楼盘ＩＤ
     * @return
     */
    @GET("Announcements/GetNewAnnouncement.ashx")
    Call<AnnouncementResp> getNewAnnouncement(
            @Query("buildingID") String buildingID
    );

    /**
     * 拉取小区公告列表信息
     *
     * @param buildingID
     * @return
     */
    @GET("Announcements/GetNewAnnouncementList.ashx")
    Call<AnnouncementsResp<AnnouncementVo>> getNewAnnouncementList(
            @Query("buildingID") String buildingID
    );

    /**
     * 公告信息详情
     *
     * @param announcementID
     * @return
     */
    @GET("Announcements/GetAnnouncementContent.ashx")
    Call<AnnouncementResp> getAnnouncementContent(
            @Query("announcementID") String announcementID
    );

    /**
     * 获取商轮播图接口
     */
    @GET("Products/GetOnProductPageProducts.ashx")
    Call<OnTopProductListResp<List<PicturesVo>>> getOnProductPageProducts();

    /**
     * 获取首页所有推荐的商品
     *
     * @return RecommendProductListResp<RecommendProductVo>
     */
    @GET("Products/GetOnMainPageProducts.ashx")
    Call<RecommendProductListResp<List<RecommendProductVo>>> getOnMainPageProducts();


    /**
     * 获取所有商品信息
     *
     * @param sortType  排序类型 1:销量 2:价格 3:上架时间
     * @param isDesc    是否是降序   0:升序 1:降序
     * @param pageIndex 显示的页数
     * @return
     */
    @GET("Products/GetProducts.ashx")
    Call<ProductListResp<List<ProductVo>>> getAllProductByType(
            @Query("sortType") int sortType,
            @Query("isDesc") int isDesc,
            @Query("pageIndex") int pageIndex
    );

    /**
     * 获取单个商品信息
     *
     * @param productID 商品ＩＤ
     * @return
     */
    @GET("Products/GetProduct.ashx")
    Call<ProductResp<ProductVo>> getProduct(@Query("productID") String productID);

    /**
     * 创建订单
     * userID 用户ID
     * products 购买产品 格式（商品ID,数量|商品ID,数量）如：
     * a49bafcc-bc3b-4143-b636-99268cf7bc41,2|854653b8-25b9-493d-b5e4-a8c74aa665ed,3
     *
     * @param userID
     * @param products
     * @param remark
     * @param addressID
     * @return
     */
    @GET("Orders/CreateOrder.ashx")
    Call<OrderInfoResp> createOrder(
            @Query("userID") String userID,
            @Query("products") String products,
            @Query("remark") String remark,
            @Query("addressID") String addressID
    );

    /**
     * 取消订单
     * @param orderID
     * @return
     */
    @GET("Orders/CancelOrder.ashx")
    Call<BaseInfoVo> cancelOrder(@Query("orderID") String orderID);

    /**
     * 确认收货
     * @param orderID
     * @return
     */
    @GET("Orders/ConfirmReceived.ashx")
    Call<BaseInfoVo> confirmReceived(@Query("orderID") String orderID);

    /**
     * 申请退款
     * @param orderID
     * @return
     */
    @GET("Orders/ApplyRefund.ashx")
    Call<BaseInfoVo> applyRefund(@Query("orderID") String orderID);

    /**
     * 获取订单详情
     * @param orderID
     * @return
     */
    @GET("Orders/GetOrderDetails.ashx")
    Call<ProductOrderVoResp<ProductOrderVo>> getOrderDetails(@Query("orderID") String orderID);

    /**
     * 添加购物车
     *
     * @param userID
     * @param productID
     * @param amount
     * @return
     */
    @GET("ShoppingCart/AddProductToShoppingCart.ashx")
    Call<BaseInfoVo> AddProductToShoppingCart(
            @Query("userID") String userID,
            @Query("productID") String productID,
            @Query("amount") int amount
    );

    /**
     * 获取购物车
     *
     * @param userID
     * @return
     */
    @GET("ShoppingCart/GetShoppingCartProducts.ashx")
    Call<ShoppingCartResp<List<ShoppingCartVo>>> getShoppingCartProducts(
            @Query("userID") String userID
    );

    /**
     * 修改购物车商品数量
     *
     * @param shoppingCartID
     * @return
     */
    @GET("ShoppingCart/EditShoppingCartProduct.ashx")
    Call<ShoppingCartResp<List<ShoppingCartVo>>> editShoppingCartProduct(
            @Query("userID") String userID,
            @Query("amount") int amount,
            @Query("shoppingCartID") String shoppingCartID
    );

    /**
     * 删除购物车产品
     *
     * @param shoppingCartID
     * @return
     */
    @GET("ShoppingCart/RemoveShoppingCartProduct.ashx")
    Call<ShoppingCartResp<List<ShoppingCartVo>>> removeShoppingCartProduct(
            @Query("userID") String userID,
            @Query("shoppingCartID") String shoppingCartID
    );

    /**
     * 商品订单微信签名
     *
     * @param orderID
     * @return
     */
    @GET("Orders/PayProductOrderByWeiChat.ashx")
    Call<WXPayVo> payProductOrderByWeiChat(@Query("orderID") String orderID);

    /**
     * 商品支付宝签名
     *
     * @param orderID
     * @return
     */
    @GET("Orders/PayProductOrderByAlipay.ashx")
    Call<AlipayVo> payProductOrderByAlipay(@Query("orderID") String orderID);

    /**
     * 用户购买商品订单列表
     * 订单状态 (0：待支付 1：待收货 2：已完成 3：退款中 4：退款完成)
     * @param userID
     * @param orderState
     * @return
     */
    @GET("Orders/GetUserOrders.ashx")
    Call<ProductOrderListResp<List<ProductOrderVo>>> getUserOrders(
            @Query("userID") String userID,
            @Query("orderState") int orderState
    );

    /**
     * 添加收货地址
     *
     * @return
     */
    @POST("Users/AddUserReceiveProductAddress.ashx")
    Call<BaseInfoVo> addUserReceiveProductAddress(
            @Body ReceiverAddressVo receiverAddressVo
    );

    /**
     * 获取收货地址
     * ReceiveProductAddressList
     *
     * @param userID
     * @return
     */
    @GET("Users/GetUserReceiveProductAddress.ashx")
    Call<ReceiveProductAddressResp<List<ReceiveProductAddressVo>>> getUserReceiveProductAddress(
            @Query("userID") String userID
    );

    /**
     * 调查主题列表
     *
     * @param buildingID
     * @return
     */
    @GET("Investigations/GetInvestigations.ashx")
    Call<InvestigationListResp<List<InvestigationVo>>> getInvestigations(
            @Query("buildingID") String buildingID
    );

    /**
     * 调查的问题和选项
     *
     * @param investigationID
     * @return
     */
    @GET("Investigations/GetInvestigationQuestions.ashx")
    Call<InvestigationQuestionListResp<List<InvestigationQuestionVo>>> getInvestigationQuestions(
            @Query("investigationID") String investigationID
    );

    /**
     * 调查的问题和选项
     *
     * @param userID         用户ID
     * @param questionID     问题ID
     * @param answerIDString 答案ID字符串 (如果遇到多选题，则用逗号隔开)
     * @param answerContent  文本答案
     *                       备注：当回答问题类型为文本时，不需要传递answerID，但需要传递answerContent。
     *                       当问题类型为单选或多选时，需要传递answerID，不需要传递answerContent
     * @return
     */
    @POST("Investigations/UploadUserAnaswer.ashx")
    Call<BaseInfoVo> uploadUserAnswer(
            @Body InvestigationAnswerVo investigationAnswerVo
    );

    /**
     * 获取我的调查
     * @param userID
     * @return
     */
    @GET("Investigations/UserInvestigations.ashx")
    Call<UserInvestigationListResp> userInvestigations(
            @Query("userID") String userID
    );

    /**
     * 获取调查统计数据
     * @param investigationID
     * @return
     */
    @GET("Investigations/GetInvestigationsStatistics.ashx")
    Call<InvestigationStatisticsResp> getInvestigationsStatistics(
            @Query("InvestigationID") String investigationID
    );

    /**
     * 物业费收费标准/月
     *
     * @param apartmentNumberID
     * @return
     */
    @GET("PropertyCosts/GetApartmentPropertyCost.ashx")
    Call<ApartmentPropertyCostResp> getApartmentPropertyCost(@Query("apartmentNumberID") String apartmentNumberID);

    /**
     * 创建缴纳物业费订单
     *
     * @param userID
     * @return
     */
    @GET("PropertyCosts/CreatePropertyCostsOrder.ashx")
    Call<OrderInfoResp> createPropertyCostsOrder(
            @Query("userID") String userID,
            @Query("apartmentNumberID") String apartmentNumberID,
            @Query("monthly") String monthly,
            @Query("beginYearMonth") String beginYearMonth,
            @Query("endYearMonth") String endYearMonth
    );

    /**
     * 支付宝缴纳物业费订单
     *
     * @param orderID
     * @return
     */
    @GET("PropertyCosts/PayPropertyCostsOrderByAlipay.ashx")
    Call<AlipayVo> payPropertyCostsOrderByAlipay(
            @Query("orderID") String orderID
    );

    /**
     * 支付宝缴纳物业费订单
     *
     * @param orderID
     * @return
     */
    @GET("PropertyCosts/PayPropertyCostsOrderByWeixin.ashx")
    Call<WXPayVo> payPropertyCostsOrderByWeixin(
            @Query("orderID") String orderID
    );

    /**
     * 用户支付物业费订单列表
     *
     * @param userID
     * @return
     */
    @GET("PropertyCosts/GetUserApartmentPropertyOrders.ashx")
    Call<UserApartmentPropertyOrdersResp<List<UserApartmentPropertyOrdersVo>>> getUserApartmentPropertyOrders(
            @Query("userID") String userID
    );

    /**
     * 获取是否可以开锁
     * @param userID
     * @param deviceID
     * @return
     */
    @GET("users/OpenDoor.ashx")
    Call<OpenDoor> openDoor(@Query("userID") String userID, @Query("deviceID") String deviceID);

    /**
     * 获取一级产品分类
     * @return
     */
    @GET("Catalogs/GetPrimaryCatalogues.ashx")
    Call<CataloguesVo<List<CataloguesVo.Catalogue>>> getPrimaryCatalogues();

    /**
     * 通过父分类所有的子分类和品牌
     * @return
     */
    @GET("Catalogs/GetSubCatalogues.ashx")
    Call<SubCatalogues<SubCatalogues.SubCatalogue>> getSubCatalogues(@Query("catalogueID") String catalogueID);

    /**
     * 子分类或子品牌下所有商品信息
     * @param subCatalogueID 子分类ID或品牌ID
     * @param sortType 排序类型 1:销量 2:价格 3:上架时间
     * @param isDesc 是否是降序   0:升序 1:降序
     * @param pageIndex 显示的页数
     * @return ProductListResp<List<ProductVo>>
     */
    @GET("Products/GetProductsBySubCatalogueID.ashx")
    Call<ProductListResp<List<ProductVo>>> getProductsBySubCatalogueID(
            @Query("subCatalogueID") int subCatalogueID,
            @Query("sortType") int sortType,
            @Query("isDesc") int isDesc,
            @Query("pageIndex") int pageIndex
    );

    /**
     * 获取所有提供服务的公司
     * @return
     */
    @GET("ServiceCompany/GetCompanies.ashx")
    Call<ServiceCompanyVo<List<ServiceCompanyVo.ServiceCompany>>> getCompanies();

    /**
     * 通过服务编码获取，服务公司具体信息
     */
    @GET("ServiceCompany/GetCompanyDetails.ashx")
    Call<ServiceCompanyDetail> getCompanyDetails(@Query("companyID") String companyID);

    /**
     * 创建服务订单
     * @param  userID 用户ID
     * @param companyID 服务公司ID
     * @param noteNumber 服务单号
     * @param totalPrice 订单总金额
     */
    @GET("ServiceOrders/CreateServiceOrder.ashx")
    Call<ServiceOrderVo<ServiceOrderVo.ServiceOrderInfo>> createServiceOrder(@Query("userID") String userID,
                                                            @Query("companyID") String companyID,
                                                            @Query("noteNumber") String noteNumber,
                                                            @Query("totalPrice") String totalPrice);
    /**
     * 获取服务订单支付宝签名
     * @param orderID
     * @return
     */
    @GET("ServiceOrders/PayServiceOrderByAlipay.ashx")
    Call<AlipayVo> payServiceOrderByAlipay(@Query("orderID") String orderID);

    /**
     * 获取服务订单微信支付签名
     * @param orderID
     * @return
     */
    @GET("ServiceOrders/PayServiceOrderByWeixin.ashx")
    Call<WXPayVo> payServiceOrderByWeixin(@Query("orderID") String orderID);

}
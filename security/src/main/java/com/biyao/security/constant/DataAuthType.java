package com.biyao.security.constant;

/**
 * @ClassName: DataAuthType
 * @Description: 数据权限类型
 * @author tyjuncai
 * @date 2018/12/24 16:22
 */
public enum DataAuthType {
    /**
     * 人员和部门数据权限
     */
    TYPE_DEPARTMENT(1L, "人员和部门数据权限"),

    /**
     * 车辆数据权限-部门
     */
    TYPE_CAR_DEPARTMENT(2L, "车辆数据权限-部门"),

    /**
     * 车辆数据权限-分组
     */
    TYPE_CAR_GROUP(3L, "车辆数据权限-分组"),

    /**
     * 设备数据权限-等级
     */
    TYPE_DEVICE_LEVEL(4L, "设备数据权限-等级"),

    /**
     * 设备数据权限-型号
     */
    TYPE_DEVICE_MODEL(5L, "设备数据权限-型号"),

    /**
     * 设备数据权限-车辆敏感信息
     */
    TYPE_DEVICE_CAR(6L, "设备数据权限-车辆敏感信息"),

    /**
     * 资产数据权限
     */
    TYPE_CAPITAL(7L, "资产数据权限"),

    /**
     * 进件报告版本权限-版本
     */
    TYPE_ENTRYSHEET_VERSION(8L, "进件报告版本权限-版本"),

    /**
     * 进件报告版本权限-业务方
     */
    TYPE_ENTRYSHEET_BUSINESS(9L, "进件报告版本权限-业务方"),

    /**
     * 渠道&订单数据权限（交易系统）
     */
    TYPE_CHANNEL_ORDER(10L, "渠道&订单数据权限"),

    /**
     * 客户数据权限（交易系统）
     */
    TYPE_CUSTOM(11L, "客户数据权限"),

    /**
     * 商品数据权限（供应链系统）
     */
    TYPE_GOODS(12L, "商品数据权限"),

	/**
	 * 库存数据权限（供应链系统）
	 */
	TYPE_STOCKS(13L, "库存数据权限"),

    /**
     * 项目管理数据权限（金融业务系统）
     */
    TYPE_PROJECT(14L, "项目管理数据权限"),

    /**
     * 资金方管理数据权限（金融业务系统）
     */
    TYPE_CAPITAL_SIDE(15L, "资金方管理数据权限"),

    /**
     * 业务方管理数据权限（金融业务系统）
     */
    TYPE_BUSINESS_SIDE(16L, "业务方管理数据权限"),

    /**
     * 业务员管理数据权限（金融业务系统）
     */
    TYPE_SALESMAN(17L, "业务员管理数据权限"),
    /**
     * FBS-放贷机构管理数据权限
     */
    TYPE_FBS_LENDERS(18L, "放贷机构管理数据权限"),
    /**
     * FBS-合作商管理数据权限
     */
    TYPE_FBS_OFFICIAL_PARTNER(19L, "合作商管理数据权限"),
    /**
     * FBS-上牌方数据权限
     */
    TYPE_FBS_CARDS(20L, "上牌方管理数据权限"),
    /**
     * FBS-产权方数据权限
     */
    TYPE_FBS_PROPERTY_RIGHT(21L, "产权方数据权限"),
    /**
     * FBS-业务保证金
     */
    TYPE_FBS_BUSINESS_DEPOSIT(22L, "业务保证金"),
    /**
     *FBS-资金投放审批
     */
    TYPE_FBS_FUND_RELEASE_APPROVAL(23L, "资金投放审批"),
    /**
     * FBS-客户还款计划
     */
    TYPE_FBS_CUSTOMER_RETURN_MONEY_PLAN(24L, "客户还款计划"),
    /**
     * PAY-客户还款明细
     */
    TYPE_PAY_CUSTOMER_RETURN_MONEY_DETAIL(25L, "客户还款明细"),
    /**
     * PAY-合作商客户还款明细
     */
    TYPE_PAY_PARTNER_CUSTOMER_RETURN_MONEY_DETAIL(26L, "合作商客户还款明细"),
    /**
     * PAY-客户还款交易流水
     */
    TYPE_PAY_CUSTOMER_RETURN_TRADE_DETAIL(27L, "客户还款交易流水"),
    /**
     * PAY-合作商代偿明细
     */
    TYPE_PAY_PARTNER_REPLACE_RETURN_DETAIL(28L, "合作商代偿明细"),
    /**
     * PAY-客户还款明细-新
     */
    TYPE_PAY_PARTNER_REPLACE_RETURN_DETAIL_NEW(29L, "客户还款明细-新"),
    /**
     * PAY-客户还款交易流水-新
     */
    TYPE_PAY_CUSTOMER_RETURN_TRADE_DETAIL_NEW(30L, "客户还款交易流水-新"),
    /**
     * FBS-合同审批列表
     */
    TYPE_CONTRACT_APPROVAL_LIST(31L, "合同审批列表"),
    /**
     * MALL-车辆管理
     */
    TYPE_MALL_CAR_LIST(32L, "车辆管理数据"),
    /**
     * USER_CENTER-渠道管理
     */
    TYPE_USER_CENTER_CHANNEL(33L, "渠道管理"),
    /**
     * AUTHORITY-账号管理
     */
    TYPE_AUTHORITY_ACCOUNT(34L, "账号管理"),
    /**
     * CO-资金产品
     */
    TYPE_CO_FUND_PRODUCT(35L, "资金产品"),
    /**
     * CO-贷后标准
     */
    TYPE_CO_LOAN_STANDARD(36L, "贷后标准"),
    /**
     * FINANCING-资产列表
     */
    TYPE_FINANCING_ASSET_LIST(37L,"资产列表"),
    /**
     * FINANCING-业务提成
     */
    TYPE_FINANCING_BUSINESS_ROYALTY(38L,"业务提成"),
    /**
     * 库融订单-订单管理
     */
    INVENTORY_FINANCING_ORDER_LIST(39L,"库融订单-订单管理"),
    /**
     * 库融进件-我的进件审批
     */
    INVENTORY_FINANCING_INCOMING_APPROVAL_SELF(40L,"库融进件-我的进件审批"),
    /**
     * 库融进件-进件审批列表
     */
    INVENTORY_FINANCING_INCOMING_APPROVAL_LIST(41L,"库融进件-进件审批列表"),
    /**
     * 合同审批-我的合同审批
     */
    INVENTORY_FINANCING_CONTRACT_APPROVAL_SELF(42L,"合同审批-我的合同审批"),
    /**
     * 合同审批-合同审批列表
     */
    INVENTORY_FINANCING_CONTRACT_APPROVAL_LIST(43L,"合同审批-合同审批列表"),
    /**
     * 法人客户管理
     */
    CORPORATE_CUSTOMER_LIST(44L,"法人客户管理"),

    /**
     * 自定义报表管理
     */
    TYPE_CUSTOM_REPORT_MANAGER(45L,"自定义报表管理"),
    /**
     * 自定义看板管理
     */
    TYPE_CUSTOM_KANBAN_MANAGER(46L,"自定义看板管理"),
    /**
     *FBS-资金投放申请
     */
    TYPE_FBS_FUND_RELEASE_APPLY(47L, "资金投放申请"),
    /**
     * 资产借阅申请
     */
    ASSET_BORROW_APPLY_LIST(48L, "资产借阅申请列表"),
    /**
     * 资产归还申请列表
     */
    ASSET_RETURN_APPLY_LIST(49L, "资产归还申请列表"),
    /**
     * 结清管理-结清管理
     */
    SETTLE_MANAGE(50L, "结清管理"),
    /**
     * 结清管理-我的结清申请
     */
    SETTLE_APPLY(51L, "我的结清申请"),
    /**
     * 结清管理-结清审批列表
     */
    SETTLE_APPROVAL_LIST(52L, "结清审批列表"),
    /**
     * 结清管理-结清款项确认
     */
    SETTLE_CONFIRM(53L, "结清款项确认"),
    /**
     * 保险管理-待保数据
     */
    INSURE_NOT(54L, "待保数据"),
    /**
     * 保险管理-保险管理-已保数据
     */
    INSURE_ALREADY(55L, "已保数据"),
    /**
     * 保险管理-待开票交易流水
     */
    TRANSACTION_FLOW(56L, "待开票交易流水"),
    /**
     * 保险管理-我的开票申请
     */
    INVOICE_APPROVAL(57L, "我的开票申请"),
    /**
     * 保险管理-开票审批列表
     */
    INVOICE_APPROVAL_LIST(58L, "开票审批列表"),
    /**
     * 保险管理-已开票登记
     */
    INVOICE_APPROVAL_AFTER(59L, "已开票登记");

    private static final EnumMap<Long, DataAuthType> enumMap;

    static {
        enumMap = new EnumMap<>(DataAuthType.class, DataAuthType::getValue);
    }

    public static DataAuthType fromValue(Long value) {
        return enumMap.fromValue(value);
    }


    private Long value;
    private String name;

    DataAuthType(Long value, String name) {
        this.value = value;
        this.name = name;
    }

    public Long getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}

package com.example.admanage.utils;

/**
 * <b>类   名：</b>ResultType<br/>
 * <b>类描述：</b>api接口实现类中返回类型枚举组织接口，可以根据应用各自定制自己的枚举类<br/>
 * <b>创建人：</b>longzhao<br/>
 * <b>创建时间：</b>2015-8-11 下午3:44:12<br/>
 * <b>修改人：</b>longzhao<br/>
 * <b>修改时间：</b>2015-8-11 下午3:44:12<br/>
 * <b>修改备注：</b><br/>
 *
 * @version 1.0<br/>
 * 
 */
public interface ResultType {
	/**
	 * 
	 * getResultCode(取得返回结果值)
	 * 
	 * @return 返回结果值
	 * @since  1.0
	 * @author longzhao
	 */
	public int getResultCode();
	
	/**
	 * 
	 * getResultMsg(取得返回结果消息内容)
	 * 
	 * @return 返回结果消息内容
	 * @since  1.0
	 * @author longzhao
	 */
	public String getResultMsg();
	
	/**
	 * 
	 * <b>类   名：</b>SimpleResultType<br/>
	 * <b>类描述：</b>简单的返回结果定义（0:成功；-1:操作失败）<br/>
	 * <b>创建人：</b>longzhao<br/>
	 * <b>创建时间：</b>2015-8-11 下午3:52:02<br/>
	 * <b>修改人：</b>longzhao<br/>
	 * <b>修改时间：</b>2015-8-11 下午3:52:02<br/>
	 * <b>修改备注：</b><br/>
	 *
	 * @version 1.0<br/>
	 *
	 */
	public static enum SimpleResultType implements ResultType {
		/**
		 * 成功
		 */
		SUCCESS(0, "成功"),
		/**
		 * 操作失败
		 */
		OPERATE_ERROR(-1, "操作失败"),
		/**
		 * 部分成功
		 */
		OPERATE_HALF_OK(-2, "部分成功"),
		/**
		 * 无权操作
		 */
		OPERATE_FORBIDDEN(-3, "无权操作");
		
		// 返回结果值
		private int resultCode;
		// 返回结果消息内容
		private String resultMsg;
		
		/**
		 * 
		 * 创建一个新的实例 SimpleResultType.
		 *
		 * @param resultCode 返回结果值
		 * @param resultMsg 返回结果消息内容
		 */
		private SimpleResultType(int resultCode, String resultMsg) {
			this.resultCode = resultCode;
			this.resultMsg = resultMsg;
		}
		
		/* (non-Javadoc)
		 * @see com.iflytek.epdcloud.epsp.utils.ResultType#getResultCode()
		 */
		@Override
		public int getResultCode() {
			return this.resultCode;
		}
		
		/* (non-Javadoc)
		 * @see com.iflytek.epdcloud.epsp.utils.ResultType#getResultMsg()
		 */
		@Override
		public String getResultMsg() {
			return this.resultMsg;
		}
	}
}

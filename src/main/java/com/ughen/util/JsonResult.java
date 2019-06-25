package com.ughen.util;

import com.ughen.constants.ResultCode;

public class JsonResult<T> {

		private int code;
		private String msg;
		private T data;

		public static <E> JsonResult build(ResultCode resultCode, E bean) {
				return new JsonResult(resultCode, bean);
		}

		public <E> JsonResult(ResultCode resultCode, T bean) {
				result(resultCode);
				this.data = (T) bean;
		}

		public static<E> JsonResult build(ResultCode resultCode) {
				return new JsonResult(resultCode);
		}

		public JsonResult(ResultCode resultCode) {
				result(resultCode);
		}

		public void result(ResultCode resultCode) {
				this.code = resultCode.getCode();
				this.msg = resultCode.getMsg();
		}

		public int getCode() {
				return code;
		}

		public void setCode(int code) {
				this.code = code;
		}

		public String getMsg() {
				return msg;
		}

		public void setMsg(String msg) {
				this.msg = msg;
		}

		public T getData() {
				return data;
		}

		public void setData(T data) {
				this.data = data;
		}
}

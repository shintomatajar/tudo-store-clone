package ae.tudomart.store.model.order.orderDetails;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelOrderPriceDetails {
	public ModelOrderPriceDetails(){

	}
	public ModelOrderPriceDetails(List<DataItem> data, boolean success, String message) {
		this.data = data;
		this.success = success;
		this.message = message;
	}

	@SerializedName("data")
	private List<DataItem> data;

	@SerializedName("success")
	private boolean success;

	@SerializedName("message")
	private String message;

	public List<DataItem> getData(){
		return data;
	}

	public boolean isSuccess(){
		return success;
	}

	public String getMessage(){
		return message;
	}

	@Override
 	public String toString(){
		return 
			"ModelOrderPriceDetails{" + 
			"data = '" + data + '\'' + 
			",success = '" + success + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}
	public class DataItem{

		@SerializedName("intDiscount")
		private Double intDiscount;

		@SerializedName("strOrderID")
		private String strOrderID;

		@SerializedName("intGrandTotal")
		private Double intGrandTotal;

		@SerializedName("intSubTotal")
		private Double intSubTotal;

		@SerializedName("intDeliveryCharge")
		private Double intDeliveryCharge;

		@SerializedName("strCreateUserId")
		private String strCreateUserId;

		public String getStrCreateUserId() {
			return strCreateUserId;
		}

		public String getStrOrderID() {
			return strOrderID;
		}

		public Double getIntDiscount(){
			return intDiscount;
		}

		public Double getIntGrandTotal(){
			return intGrandTotal;
		}

		public Double getIntSubTotal(){
			return intSubTotal;
		}

		public Double getIntDeliveryCharge(){
			return intDeliveryCharge;
		}

		@Override
		public String toString(){
			return
					"DataItem{" +
							"intDiscount = '" + intDiscount + '\'' +
							",intGrandTotal = '" + intGrandTotal + '\'' +
							",intSubTotal = '" + intSubTotal + '\'' +
							",intDeliveryCharge = '" + intDeliveryCharge + '\'' +
							"}";
		}
	}
}
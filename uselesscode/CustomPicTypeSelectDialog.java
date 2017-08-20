package com.onemena.widght;


import android.widget.PopupWindow;


public class CustomPicTypeSelectDialog {
	
	

	private static class IntanceHolder {
		private static final CustomPicTypeSelectDialog INSTANCE = new CustomPicTypeSelectDialog();
	}

	private CustomPicTypeSelectDialog() {
	}

	public static final CustomPicTypeSelectDialog getInstance() {
		return IntanceHolder.INSTANCE;
	}

	private PopupWindow headIconPopWin;
	
	private SelectListener listener;

	public SelectListener getListener() {
		return listener;
	}

	public void setListener(SelectListener listener) {
		this.listener = listener;
	}
	


	public void dismissDialog() {
		if (headIconPopWin != null)
			headIconPopWin.dismiss();
	}

	public interface SelectListener{
		void OnNormalRegClick();
		void OnExportRegClick();
	}
}

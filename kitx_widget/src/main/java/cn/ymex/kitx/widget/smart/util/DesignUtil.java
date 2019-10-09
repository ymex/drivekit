package cn.ymex.kitx.widget.smart.util;

import android.view.View;
import android.view.ViewGroup;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.appbar.AppBarLayout;

import cn.ymex.kitx.widget.smart.api.RefreshKernel;
import cn.ymex.kitx.widget.smart.listener.CoordinatorLayoutListener;

/**
 * Design 兼容包缺省尝试
 * Created by SCWANG on 2018/1/29.
 */

public class DesignUtil {

    public static void checkCoordinatorLayout(View content, RefreshKernel kernel, final CoordinatorLayoutListener listener) {
        try {//try 不能删除，不然会出现兼容性问题
            if (content instanceof CoordinatorLayout) {
                kernel.getRefreshLayout().setEnableNestedScroll(false);
                ViewGroup layout = (ViewGroup) content;
                for (int i = layout.getChildCount() - 1; i >= 0; i--) {
                    View view = layout.getChildAt(i);
                    if (view instanceof AppBarLayout) {
                        ((AppBarLayout) view).addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                            @Override
                            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                                listener.onCoordinatorUpdate(
                                        verticalOffset >= 0,
                                        (appBarLayout.getTotalScrollRange() + verticalOffset) <= 0);
                            }
                        });
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}

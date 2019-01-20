package me.businesscomponent.activity;

import android.arch.lifecycle.Lifecycle;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.from.business.http.component.AppComponent;
import com.from.business.http.integration.IRepositoryManager;
import com.from.business.http.retrofiturlmanager.RetrofitUrlManager;
import com.from.business.http.utils.HttpModuleUtils;
import com.uber.autodispose.AutoDisposeConverter;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import io.rx_cache2.Reply;
import me.businesscomponent.BaseApplication;
import me.businesscomponent.cache.CacheProviders;
import me.businesscomponent.entity.GankBaseResponse;
import me.businesscomponent.entity.GankItemBean;
import me.businesscomponent.http.GankService;
import me.businesscomponent.utils.RxLifecycleUtils;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;
import timber.log.Timber;

import static me.businesscomponent.http.GankService.GANK_DOMAIN;
import static me.businesscomponent.http.GankService.GANK_DOMAIN_NAME;

/**
 * @author Vea
 * @since 2019-01-16
 */
public class HttpExampleActivity extends AppCompatActivity {

    private HttpPresenterOrViewModel pm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RetrofitUrlManager.getInstance().putDomain(GANK_DOMAIN_NAME, GANK_DOMAIN);

        AppComponent appComponent = HttpModuleUtils.obtainAppComponentFromContext(this);

        pm = new HttpPresenterOrViewModel(appComponent.application());
        pm.setLifecycle(getLifecycle());

        pm.getUserList(false);
        pm.getGirlList();

    }

    public <T> AutoDisposeConverter<T> bindLifecycle() {
        return RxLifecycleUtils.bindLifecycle(this);
    }


    @Override
    protected void onStop() {
        super.onStop();
        Lifecycle.State currentState = getLifecycle().getCurrentState();
        Timber.tag(BaseApplication.TAG).d("stop:" + currentState.name());
        pm.stop();
    }
}

package com.example.FM_SHOP.uiUser;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.FM_SHOP.Adapter.ProductAdapterUser;
import com.example.FM_SHOP.Adapter.SliderAdapterExample;
import com.example.FM_SHOP.Api.ApiService;
import com.example.FM_SHOP.Api.RetrofitClient;
import com.example.FM_SHOP.R;
import com.example.FM_SHOP.model.Product;
import com.example.FM_SHOP.model.SliderItem;
import com.example.FM_SHOP.uiUser.DetailProductActivity;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreFragment extends Fragment {

    Toolbar mToolbar;
    RecyclerView mRecyclerViewProduct;
    SliderView sliderView;
    EditText mEditTextSearch;

    List<Product> productListStore;
    ProductAdapterUser mAdapterUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mToolbar = view.findViewById(R.id.tbHome);

        mEditTextSearch = mToolbar.findViewById(R.id.edtSearch);

        mEditTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });


        mRecyclerViewProduct = view.findViewById(R.id.rcHomeUser);
        sliderView = view.findViewById(R.id.imageSlider);

        setSlide();
        setListProduct();

        return view;

    }

    private void setSlide() {
        List<SliderItem> sliderItems = new ArrayList<>();
        sliderItems.add(new SliderItem("https://chupanhvn.s3.ap-southeast-1.amazonaws.com/wp-content/uploads/2015/12/01071610/chupanhthoitrangcatinhsangtrong-930x620.jpg"));
        sliderItems.add(new SliderItem("https://lh3.googleusercontent.com/proxy/muzkVvpFgu4nJu_he2wAePs8sOOeSkK-OA-MnB5h7-Ngvw4y1pVLjBdPHSbmqIRhVvqSN1GU-QlyBIkmOGnfNUlx_V90C9N0-BeBJ93zHIl5lXErL90kMa3a"));
        sliderItems.add(new SliderItem("https://bytuong.com/wp-content/uploads/2019/11/mo-hinh0kinh-doanh-cho-thue-pg-lai-cao-xu0huong-moi-thoi-hien-dai-bytuong-com.jpg"));
        sliderItems.add(new SliderItem("https://studios.vn/wp-content/uploads/2018/01/studio-chup-anh-thoi-trang-dep-tai-ha-noi8.jpg"));
        sliderItems.add(new SliderItem("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQHlzjAACqCJlsT5WcvI6Z64UeAs_y470g3uw&usqp=CAU"));
        sliderItems.add(new SliderItem("http://media.istockphoto.com/photos/mens-clothes-picture-id531325796?k=6&m=531325796&s=170667a&w=0&h=zTFxchziLB0MZssBEO6nOPau8JVd7V66pSxfW8OYyPI="));
        sliderItems.add(new SliderItem("https://www.way.com.vn/vnt_upload/news/10_2019/thuong-hieu-thoi-trang-nam-noi-tieng-o-viet-nam.jpg"));
        sliderView.setSliderAdapter(new SliderAdapterExample(getContext(), sliderItems));
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.startAutoCycle();

    }

    private void setListProduct() {

        ApiService service = RetrofitClient.getInstance().create(ApiService.class);
        Call<List<Product>> listCall = service.productList();
        listCall.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {

                productListStore = response.body();
                setAdapter(productListStore);
            }
            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("ERROR", "" + t);
            }
        });
    }

    public void setAdapter(List<Product> productList) {

        mAdapterUser = new ProductAdapterUser(productList,getContext());

        GridLayoutManager manager = new GridLayoutManager(getContext(),
                2, RecyclerView.VERTICAL, false);
        mRecyclerViewProduct.setAdapter(mAdapterUser);
        mRecyclerViewProduct.setLayoutManager(manager);

        mAdapterUser.setItemClick(new ProductAdapterUser.OnclickListener() {
           @Override
           public void onclickItem(int position, View view) {
               Toast.makeText(getContext(), "lollooo"+position, Toast.LENGTH_SHORT).show();
           }
       });

    }


    private void filter(String Text) {
        ArrayList<Product> foodyList = new ArrayList<>();
        for (Product foody : productListStore) {
            if (foody.getName().toLowerCase().contains(Text.toLowerCase())) {
                foodyList.add(foody);
            }
        }
        mAdapterUser.FilterList(foodyList);
    }
}

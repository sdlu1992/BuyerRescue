package lu.shaode.buyerrescue.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import lu.shaode.buyerrescue.R;
import lu.shaode.buyerrescue.adapter.AdapterAddressList;
import lu.shaode.buyerrescue.ui.dummy.ContentAddressList;
import lu.shaode.netsupport.BizManager;
import lu.shaode.netsupport.listener.ApiListener;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link lu.shaode.buyerrescue.ui.FragmentParentList.OnFragmentInteractionListener}
 * interface.
 */
public class FragmentAddressChoose extends FragmentParentList{

    private final String TAG = ((Object) this).getClass().getSimpleName();

    Button btAddressNew;
    public AdapterAddressList mAdapter;

    public static FragmentAddressChoose newInstance() {
        return new FragmentAddressChoose();
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentAddressChoose() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new AdapterAddressList(getActivity(), ContentAddressList.ITEMS);
        setListAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(getLayoutContent(), container, false);
        btAddressNew = (Button) view.findViewById(R.id.frag_address_add);
        btAddressNew.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getAddressList();
    }

    @Override
    protected int getLayoutContent() {
        return R.layout.fragment_fragment_address_list;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Log.e(TAG + " sdlu", "position= " + position);
        Intent intent = new Intent();
        intent.putExtra("address", ContentAddressList.ITEMS.get(position).jsonObject.toString());
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    public void getAddressList(){
        btAddressNew.post(new Runnable() {
            @Override
            public void run() {
                ((ActParent) getActivity()).showDialogLoading();
            }
        });
        BizManager.getInstance(getActivity()).getAddressList(new AddressApiListener());
    }

    class AddressApiListener implements ApiListener{

        @Override
        public void success(JSONObject jsonObject) {
            Log.e(TAG + " sdlu", "jsonObject= " + jsonObject.toString());
            int responseCode = 2;
            try {
                responseCode = jsonObject.getInt("response");
                switch (responseCode){
                    case 1:
                        ContentAddressList.ITEMS.clear();
                        int length = jsonObject.getInt("len");
                        if (length != 0){
                            JSONArray addresses= jsonObject.getJSONArray("address_list");
                            for (int i = 0; i < addresses.length(); i++){
                                JSONObject foo = addresses.getJSONObject(i);
                                ContentAddressList.Address address = new ContentAddressList.Address(foo);
                                ContentAddressList.ITEMS.add(address);
                            }
                            mAdapter.notifyDataSetChanged();
                        }
                        break;
                    case 2:
                        break;
                }

                ((ActParent)getActivity()).dismissDialogLoading();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void error(String string) {
            Log.e(TAG + " sdlu", "string= " + string);
            ((ActParent)getActivity()).dismissDialogLoading();

        }
    }
}

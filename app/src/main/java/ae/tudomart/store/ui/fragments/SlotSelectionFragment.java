package ae.tudomart.store.ui.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import ae.matajar.store.R;
import ae.matajar.store.databinding.FragmentSlotSelectionBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import ae.tudomart.store.api.SlotsAPI;
import ae.tudomart.store.model.ModelTimeSlotResponse;
import ae.tudomart.store.utils.AppUtils;
import ae.tudomart.store.utils.ResponseCallback;

public class SlotSelectionFragment extends BottomSheetDialogFragment {
    SlotSelectionCallback callback;

    public SlotSelectionFragment(SlotSelectionCallback callback) {
        this.callback = callback;
    }

    private FragmentSlotSelectionBinding binding;
    String selectedDate = "";
    String selectedSlotType = "NORMAL";
    SlotSelector slotSelector;
    ModelTimeSlotResponse.Data selectedSlot = null;

    public SlotSelectionFragment(SlotSelector slotSelector) {
        this.slotSelector = slotSelector;
    }

    public SlotSelectionFragment() {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI();
    }

    private void initUI() {
        binding.radioType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_express) {
                    selectedSlotType = "EXPRESS";
                } else {
                    selectedSlotType = "NORMAL";
                }
            }
        });
        binding.layDate.setOnClickListener(v -> showDatePickerDialog());
        binding.radioExpress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        binding.btnFindSlots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedDate.isEmpty()) {
                    Toast.makeText(requireContext(), "Select a date", Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONObject req = new JSONObject();
                try {
                    req.put("strType", selectedSlotType);
                    req.put("strFromDate", selectedDate);
                    req.put("strToDdate", selectedDate);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                AppUtils.INSTANCE.showLoaderDialog(requireActivity(), "Please wait..");
                new SlotsAPI(requireActivity()).getSlotsWithDate(req, new ResponseCallback<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        AppUtils.INSTANCE.hideLoaderDialog();
                        ModelTimeSlotResponse data = new Gson().fromJson(jsonObject.toString(), ModelTimeSlotResponse.class);
                        if (data.getData() == null) return;
                        callback.onSlotFetched(data.getData());
                    }

                    @Override
                    public void onError(String error) {
                        AppUtils.INSTANCE.hideLoaderDialog();
                        Toast.makeText(requireContext(), "error " + error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void showDatePickerDialog() {
        final Calendar newCalendar = Calendar.getInstance();
        new DatePickerDialog(requireActivity(), (view, year, monthOfYear, dayOfMonth) -> {
            Calendar newDate = Calendar.getInstance();
            newDate.set(year, monthOfYear, dayOfMonth);
            selectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(newDate.getTime());
            binding.txtDate.setText(selectedDate);
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSlotSelectionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public interface SlotSelector {
        void onSlotSelected(String slotId);
    }

    public interface SlotSelectionCallback {
        void onSlotSelected(String slotID);

        void onSlotFetched(List<ModelTimeSlotResponse.Data> slots);
    }
}
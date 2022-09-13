package ae.tudomart.store.ui.activities.ordersubstitute

import ae.tudomart.store.api.OrderAPI
import ae.tudomart.store.api.SearchAPI
import ae.tudomart.store.helpers.sharedPref.UserSessionManager
import ae.tudomart.store.utils.*
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ae.matajar.store.databinding.FragmentSearchResultBinding
import ae.matajar.store.databinding.NetworkViewBinding
import org.json.JSONObject


class SearchResultFragment : Fragment() {

    private var _binding: FragmentSearchResultBinding? = null
    private val binding get() = _binding!!

    private var _networkViewBinding: NetworkViewBinding? = null
    private val networkViewBinding get() = _networkViewBinding!!

    var isLoading = false
    var isFullyLoaded = false

    var PAGE_LIMIT = 20
    var SKIP_COUNT = 0


    private lateinit var adapter: SearchResultProductAdapter
    private lateinit var selectedAdapter: SubstitutionSelectedItemsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchResultBinding.inflate(inflater, container, false)
        _networkViewBinding = NetworkViewBinding.bind(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        getResult()
    }

    private fun getResult() {
        if (isLoading) {
            return
        }
        val strSubCategoryId = (activity as? SearchProductsActivity)?.strSubCategoryId
        val query = binding.edSearch.text.toString().trim()
        if (query.isNullOrEmpty() && strSubCategoryId.isNullOrEmpty()) return

//list pagination fox// if (query.length == 1) SKIP_COUNT = 0

        val stores = Gson().fromJson<List<String>>(
            Utils.getStoreJson(UserSessionManager(activity).shopIdArray).toString(),
            object : TypeToken<List<String>>() {}.type
        )

        val request = ModelSearchRequest()
        request.intPageLimit = PAGE_LIMIT
        request.intPageLimit = PAGE_LIMIT
        request.intSkipCount = SKIP_COUNT
        request.arrayStore = stores
        request.strSearchTags = query
        request.strLoginUserId = UserSessionManager(context).userDetails["userid"]
        request.strSubCategoryId = if (query.isEmpty()) strSubCategoryId else null


        isLoading = true
        SearchAPI(requireActivity()).getSearchResult(
            request,
            object : ResponseCallback<MutableList<ModelSubCategoryProductsResponse.Data>> {
                override fun onSuccess(obj: MutableList<ModelSubCategoryProductsResponse.Data>?) {
                    isLoading = false
                    if (isFirstRequest()) {
                        networkViewBinding.vLoading.hide()
                        networkViewBinding.vError.hide()
                        adapter.clear()
                    }
                    adapter.updateData(obj)
                }

                override fun onError(error: String?) {
                    isLoading = false
                    if (isFirstRequest()) {
                        networkViewBinding.vLoading.hide()
                        networkViewBinding.vError.show()
                        networkViewBinding.txtErrorMessage.text = error ?: ""
                        networkViewBinding.btnRetry.setOnClickListener {
                            getResult()
                        }
                    }
                    isFullyLoaded = true
                }

            })
    }

    fun isFirstRequest() = SKIP_COUNT == 0

    private fun setupUI() {
        selectedAdapter = SubstitutionSelectedItemsAdapter { index, item ->
            adapter.removeItem(item)
            binding.recommendItemsCard.isVisible = selectedAdapter.data.isNotEmpty()
        }
        binding.recommendItems.adapter = selectedAdapter
        binding.edSearch.textChanged(onTextChanged = { _, _, _, _ ->
            getResult()
        })
        adapter = SearchResultProductAdapter(requireActivity(), { item, position ->
            selectedAdapter.addItem(item)
            binding.recommendItemsCard.isVisible = adapter.data.isNotEmpty()
        }, { index, item ->
            selectedAdapter.removeItem(item)
            binding.recommendItemsCard.isVisible = adapter.data.isNotEmpty()
        })
        binding.apply {
            binding.rvProductList.adapter = adapter
            finish.setOnClickListener {
                Log.d("Res", "finish setonclikcklistner working")
                val prods = (requireActivity() as SearchProductsActivity).products
                val order_id = (requireActivity() as SearchProductsActivity).order_id
                prods?.let { outOfStockProducts ->
                    val recommendedItems = selectedAdapter.data.map {
                        it.pkProductId
                    }

                    val req = OrderSubstituteRequest(
                        listOf(outOfStockProducts),
                        recommendedItems,
                        order_id
                    )

                    Utility.showLoaderDialog(requireActivity())
                    OrderAPI(requireActivity()).initSubstitute(req,
                        object : ResponseCallback<JSONObject> {
                            override fun onSuccess(jsonObject: JSONObject?) {
                                Utility.hideLoaderDialog()
                                requireActivity().finish()
                                Toast.makeText(
                                    requireContext(),
                                    "Out of stock items updated",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }

                            override fun onError(error: String?) {
                                Utility.hideLoaderDialog()
                                Log.d("Res", "product_list_error " + error.toString())
                            }

                        })

                }

            }
        }
        binding.rvProductList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!isLoading && !isFullyLoaded) {
                        SKIP_COUNT += PAGE_LIMIT
                        getResult()
                        Log.d("Loads Page", "${SKIP_COUNT / PAGE_LIMIT}")
                    }
                }
            }
        })
    }

}

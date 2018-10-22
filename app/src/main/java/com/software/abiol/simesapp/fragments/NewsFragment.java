package com.software.abiol.simesapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.software.abiol.simesapp.Adapters.BlogRecyclerAdapter;
import com.software.abiol.simesapp.Adapters.GalleryAdapter;
import com.software.abiol.simesapp.R;
import com.software.abiol.simesapp.models.BlogPost;
import com.software.abiol.simesapp.models.GalleryItem;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment {

    private List<GalleryItem> galleryItems;
    private RecyclerView recyclerView;
    GalleryAdapter galleryAdapter;


    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private Boolean isFirstPageFirstLoad = true;

    private DocumentSnapshot lastVisible;

    public NewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        view = inflater.inflate(R.layout.fragment_gallery, container, false);

        recyclerView = view.findViewById(R.id.gallery_recycler);

        galleryItems = new ArrayList<>();



        firebaseAuth = FirebaseAuth.getInstance();

        galleryAdapter = new GalleryAdapter(galleryItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(galleryAdapter);

        if(firebaseAuth.getCurrentUser() != null) {

            firebaseFirestore = FirebaseFirestore.getInstance();

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    Boolean reachedBottom = !recyclerView.canScrollVertically(1);

                    if(reachedBottom){

                        String desc = lastVisible.getString("desc");

                        Toast.makeText(container.getContext(), "Reached: " + desc, Toast.LENGTH_LONG).show();

                        loadMorePost();

                    }

                }
            });

            Query firstQuery = firebaseFirestore.collection("News").orderBy("timestamp", Query.Direction.DESCENDING).limit(1);
            firstQuery.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                    if (!documentSnapshots.isEmpty()) {

                        if (isFirstPageFirstLoad) {

                            lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);

                            galleryItems.clear();

                        }


                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                            if (doc.getType() == DocumentChange.Type.ADDED) {

                                String blogPostId = doc.getDocument().getId();
                                GalleryItem galleryItem = doc.getDocument().toObject(GalleryItem.class).withId(blogPostId);

                                if (isFirstPageFirstLoad) {

                                    galleryItems.add(galleryItem);

                                } else {

                                    galleryItems.add(0, galleryItem);

                                }

                                galleryAdapter.notifyDataSetChanged();

                            }
                        }

                        isFirstPageFirstLoad = false;

                    }
                }
            });

        }

        // Inflate the layout for this fragment
        return view;
    }


    public void loadMorePost(){

        Query nextQuery = firebaseFirestore.collection("Post").
                orderBy("timestamp", Query.Direction.DESCENDING)
                .startAfter(lastVisible)
                .limit(3);

        nextQuery.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                if(!documentSnapshots.isEmpty()){

                    lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                        if (doc.getType() == DocumentChange.Type.ADDED) {

                            String blogPostId = doc.getDocument().getId();
                            GalleryItem galleryItem = doc.getDocument().toObject(BlogPost.class).withId(blogPostId);
                            galleryItems.add(galleryItem);

                            galleryAdapter.notifyDataSetChanged();

                        }
                    }


                }
            }
        });

    }

}

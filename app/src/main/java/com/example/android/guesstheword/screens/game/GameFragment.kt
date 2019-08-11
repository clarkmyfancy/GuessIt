/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.guesstheword.screens.game

import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.example.android.guesstheword.R
import com.example.android.guesstheword.databinding.GameFragmentBinding

/**
 * Fragment where the game is played
 */
class GameFragment : Fragment() {

     // this fragment needs to know about the ViewModel
    private lateinit var viewModel: GameViewModel

    private lateinit var binding: GameFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.game_fragment,
                container,
                false
        )

        // never construct viewmodels yourslef, you have the lifecycle library do this for you
                                            // pass in the fragment
                                                        // specific viewmodel class you want
        viewModel = ViewModelProviders.of(this).get(GameViewModel::class.java)

        // here we are passing in the gameViewModel object to the binding object
        // doing this allows us to get rid of some code (AA)
        binding.gameViewModel = viewModel
        // allows you to use LiveData to auto update the data binding layouts
            // because of this code can do (BB)
        binding.setLifecycleOwner(this)

        // !!!! becuase we set up the onclick listeners in the xml, we dont need these anymore!
        // in this way, we remove the fragment as the intermediary that used to set up the onclick listeners
        // instead we have the views (in xml) communicate direclty with the viewModel
//  (AA)      binding.correctButton.setOnClickListener {
//  (AA)          viewModel.onCorrect()
//  (AA)      }
//  (AA)      binding.skipButton.setOnClickListener {
//  (AA)         viewModel.onSkip()
//  (AA)     }

        // becuase you added the string formatting to teh score in xml, this can now be deleted
//            // the first arg is a lifecycle owner, which is the current fragment
//            // the second arg is an ananymous observer object, will get called whenever the value changes
//        viewModel.score.observe(this, Observer { newScore ->
//            binding.scoreText.text = newScore.toString()
//                // after this is added, remove all of the calls to updateScoretext which we just removed
//
//        })

        // can get rid of this because of setting the binding.lifecycleowner to this
        // after changing the xml to automatically grab the most recent version of word from
            // the LiveData word in the GameViewModel this can go away
// (BB)       viewModel.word.observe(this, Observer { newWord ->
// (BB)          binding.wordText.text = newWord
// (BB)      })

        viewModel.eventGameFinish.observe(this, Observer { hasFinished ->
            if (hasFinished) {
                gameFinished()
                viewModel.onGameFinishComplete()
            }
        })

        // removed this becasue of databinding finesse #@dope shit
//        viewModel.currentTime.observe(this, Observer { newElapsedTime ->
//            binding.timerText.text = DateUtils.formatElapsedTime(newElapsedTime)
//        })

        return binding.root

    }

    /**
     * Called when the game is finished
     */
    private fun gameFinished() {
                                            // in the parameter passed in, use the elvis operator
                                            // it is a null check that says if it is null, pass 0 instead
        val action = GameFragmentDirections.actionGameToScore(viewModel.score.value ?: 0)
        findNavController(this).navigate(action)
    }
}

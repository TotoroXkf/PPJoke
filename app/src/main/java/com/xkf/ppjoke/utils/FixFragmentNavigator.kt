package com.xkf.ppjoke.utils

import android.content.Context
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import java.util.*

@Navigator.Name("fixFragment")
class FixFragmentNavigator(
    private val context: Context,
    private val manager: FragmentManager,
    private val containerId: Int
) : FragmentNavigator(context, manager, containerId) {
    override fun navigate(
        destination: Destination,
        args: Bundle?,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ): NavDestination? {
        if (manager.isStateSaved) {
            return null
        }
        var className = destination.className
        if (className[0] == '.') {
            className = context.packageName + className
        }
        
        val ft = manager.beginTransaction()
        
        var enterAnim = navOptions?.enterAnim ?: -1
        var exitAnim = navOptions?.exitAnim ?: -1
        var popEnterAnim = navOptions?.popEnterAnim ?: -1
        var popExitAnim = navOptions?.popExitAnim ?: -1
        if (enterAnim != -1 || exitAnim != -1 || popEnterAnim != -1 || popExitAnim != -1) {
            enterAnim = if (enterAnim != -1) enterAnim else 0
            exitAnim = if (exitAnim != -1) exitAnim else 0
            popEnterAnim = if (popEnterAnim != -1) popEnterAnim else 0
            popExitAnim = if (popExitAnim != -1) popExitAnim else 0
            ft.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim)
        }
        
        val currentFragment = manager.primaryNavigationFragment
        if (currentFragment != null) {
            ft.hide(currentFragment)
        }
        var frag = manager.findFragmentByTag(destination.id.toString())
        if (frag == null) {
            frag = instantiateFragment(context, manager, className, args)
            frag.arguments = args
            ft.add(containerId, frag, destination.id.toString())
        } else {
            ft.show(frag)
        }
        ft.setPrimaryNavigationFragment(frag)
        
        val field = FragmentNavigator::class.java.getDeclaredField("mBackStack")
        field.isAccessible = true
        val mBackStack = field.get(this) as ArrayDeque<Int>
        @IdRes val destId = destination.id
        val initialNavigation = mBackStack.isEmpty()
        val isSingleTopReplacement = (navOptions != null && !initialNavigation
                && navOptions.shouldLaunchSingleTop()
                && mBackStack.peekLast() == destId)
        
        val isAdded: Boolean
        isAdded = if (initialNavigation) {
            true
        } else if (isSingleTopReplacement) {
            if (mBackStack.size > 1) {
                manager.popBackStack(
                    generateBackStackName(mBackStack.size, mBackStack.peekLast()),
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                )
                ft.addToBackStack(generateBackStackName(mBackStack.size, destId))
            }
            false
        } else {
            ft.addToBackStack(generateBackStackName(mBackStack.size + 1, destId))
            true
        }
        if (navigatorExtras is Extras) {
            for ((key, value) in navigatorExtras.sharedElements) {
                ft.addSharedElement(key!!, value!!)
            }
        }
        ft.setReorderingAllowed(true)
        ft.commit()
        return if (isAdded) {
            mBackStack.add(destId)
            destination
        } else {
            null
        }
    }
    
    private fun generateBackStackName(backStackIndex: Int, destId: Int): String {
        return "$backStackIndex-$destId"
    }
}
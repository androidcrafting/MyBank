package com.example.mybank.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mybank.presentation.auth.AuthViewModel
import com.example.mybank.presentation.auth.LoginScreen
import com.example.mybank.presentation.auth.RegisterScreen
import com.example.mybank.presentation.auth.SplashScreen
import com.example.mybank.presentation.home.HomeScreen
import com.example.mybank.presentation.accounts.AccountsScreen
import com.example.mybank.presentation.cards.CardsScreen
import com.example.mybank.presentation.notifications.NotificationsScreen
import com.example.mybank.presentation.settings.SettingsScreen
import com.example.mybank.presentation.transfers.TransfersScreen
import com.example.mybank.presentation.transactions.AllTransactionsScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Splash.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Splash.route) {
            val authViewModel: AuthViewModel = hiltViewModel()
            val authState by authViewModel.authState.collectAsState()
            
            SplashScreen(
                isAuthenticated = authState.isAuthenticated,
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToForgotPassword = { navController.navigate(Screen.ForgotPassword.route) }
            )
        }
        
        composable(Screen.Register.route) {
            RegisterScreen(
                onNavigateToLogin = { navController.popBackStack() },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToAccounts = { navController.navigate(Screen.Accounts.route) },
                onNavigateToNotifications = { navController.navigate(Screen.Notifications.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                onNavigateToAccountDetails = { accountId ->
                    navController.navigate(Screen.AccountDetails.createRoute(accountId))
                },
                onNavigateToCards = { navController.navigate(Screen.Cards.route) },
                onNavigateToTransfers = { navController.navigate(Screen.Transfers.route) },
                onNavigateToAllTransactions = { navController.navigate(Screen.AllTransactions.route) }
            )
        }
        
        composable(Screen.Accounts.route) {
            AccountsScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToAccountDetails = { accountId ->
                    navController.navigate(Screen.AccountDetails.createRoute(accountId))
                }
            )
        }
        
        composable(
            route = Screen.AccountDetails.route,
            arguments = listOf(navArgument("accountId") { type = NavType.StringType })
        ) {
            // AccountDetailsScreen - to be implemented
        }
        
        composable(
            route = Screen.Transactions.route,
            arguments = listOf(navArgument("accountId") { type = NavType.StringType })
        ) {
            // TransactionsScreen - to be implemented
        }
        
        composable(Screen.Notifications.route) {
            NotificationsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Cards.route) {
            CardsScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Cards.route) { inclusive = true }
                    }
                },
                onNavigateToTransfers = {
                    navController.navigate(Screen.Transfers.route)
                },
                onNavigateToMore = {
                    navController.navigate(Screen.Settings.route)
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                },
                onNavigateToAccounts = {
                    navController.navigate(Screen.Accounts.route)
                },
                onAddCard = {
                    navController.navigate(Screen.Accounts.route)
                }
            )
        }
        
        composable(Screen.Transfers.route) {
            TransfersScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Transfers.route) { inclusive = true }
                    }
                },
                onNavigateToCards = {
                    navController.navigate(Screen.Cards.route) {
                        popUpTo(Screen.Transfers.route) { inclusive = true }
                    }
                },
                onNavigateToMore = {
                    navController.navigate(Screen.Settings.route)
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                },
                onNavigateToAccounts = {
                    navController.navigate(Screen.Accounts.route)
                }
            )
        }
        
        composable(Screen.AllTransactions.route) {
            AllTransactionsScreen(
                onNavigateBack = { navController.popBackStack() },
                onTransactionClick = { transactionId ->
                    navController.navigate(Screen.TransactionDetails.createRoute(transactionId))
                }
            )
        }
    }
}

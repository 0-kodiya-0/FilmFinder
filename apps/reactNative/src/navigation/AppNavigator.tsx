import React from 'react';
import { createStackNavigator } from '@react-navigation/stack';
import MainScreen from '../screens/MainScreen';
import MovieSearchScreen from '../screens/MovieSearchScreen';
import ActorSearchScreen from '../screens/ActorSearchScreen';
import ExtendedSearchScreen from '../screens/ExtendedSearchScreen';

export type RootStackParamList = {
    Main: undefined;
    MovieSearch: undefined;
    ActorSearch: undefined;
    ExtendedSearch: undefined;
};

const Stack = createStackNavigator<RootStackParamList>();

const AppNavigator: React.FC = () => {
    return (
        <Stack.Navigator
            initialRouteName="Main"
            screenOptions={{
                headerShown: false,
                gestureEnabled: true,
                cardStyleInterpolator: ({ current, layouts }) => {
                    return {
                        cardStyle: {
                            transform: [
                                {
                                    translateX: current.progress.interpolate({
                                        inputRange: [0, 1],
                                        outputRange: [layouts.screen.width, 0],
                                    }),
                                },
                            ],
                        },
                    };
                },
            }}
        >
            <Stack.Screen name="Main" component={MainScreen} />
            <Stack.Screen name="MovieSearch" component={MovieSearchScreen} />
            <Stack.Screen name="ActorSearch" component={ActorSearchScreen} />
            <Stack.Screen name="ExtendedSearch" component={ExtendedSearchScreen} />
        </Stack.Navigator>
    );
};

export default AppNavigator;
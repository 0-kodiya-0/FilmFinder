import React, { useState } from 'react';
import {
    View,
    Text,
    TouchableOpacity,
    StyleSheet,
} from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { useNavigation } from '@react-navigation/native';
import { StackNavigationProp } from '@react-navigation/stack';
import LoadingIndicator from '../components/LoadingIndicator';
import FloatingSnackbar from '../components/FloatingSnackbar';
import MovieRepository from '../data/repository/MovieRepository';
import { PREDEFINED_MOVIES } from '../utils/Constants';
import { RootStackParamList } from '../navigation/AppNavigator';
import { useOrientation } from '../hooks/useOrientation';

type MainScreenNavigationProp = StackNavigationProp<RootStackParamList, 'Main'>;

const MainScreen: React.FC = () => {
    const navigation = useNavigation<MainScreenNavigationProp>();
    const [isAddingMovies, setIsAddingMovies] = useState(false);
    const [showSnackbar, setShowSnackbar] = useState(false);
    const [snackbarMessage, setSnackbarMessage] = useState('');

    // Use the orientation hook - this will trigger re-renders on orientation change
    const { isLandscape, width, height } = useOrientation();

    // Debug log to see if orientation is being detected
    console.log('MainScreen render:', { isLandscape, width, height });

    const handleAddMoviesToDB = async () => {
        setIsAddingMovies(true);
        try {
            await MovieRepository.insertMovies(PREDEFINED_MOVIES);
            setSnackbarMessage('Movies added to database successfully');
            setShowSnackbar(true);
        } catch (error) {
            console.error('Error adding movies:', error);
            setSnackbarMessage('Error adding movies to database');
            setShowSnackbar(true);
        } finally {
            setIsAddingMovies(false);
        }
    };

    const NavigationButton: React.FC<{
        title: string;
        onPress: () => void;
        isPrimary?: boolean;
        isLoading?: boolean;
    }> = ({ title, onPress, isPrimary = false, isLoading = false }) => (
        <TouchableOpacity
            style={[
                styles.navigationButton,
                isPrimary && styles.primaryButton,
                // Apply landscape styles based on current orientation
                isLandscape && styles.landscapeButton,
            ]}
            onPress={onPress}
            disabled={isLoading}
        >
            {isLoading ? (
                <LoadingIndicator size="small" />
            ) : (
                <Text
                    style={[
                        styles.navigationButtonText,
                        isPrimary && styles.primaryButtonText,
                    ]}
                >
                    {title}
                </Text>
            )}
        </TouchableOpacity>
    );

    const AppTitle: React.FC = () => (
        <View style={[
            styles.titleContainer,
            isLandscape && styles.landscapeTitleContainer
        ]}>
            <Text style={[
                styles.appTitle,
                isLandscape && styles.landscapeAppTitle
            ]}>
                Film Finder
            </Text>
            <Text style={[
                styles.appSubtitle,
                isLandscape && styles.landscapeAppSubtitle
            ]}>
                Discover and explore your favorite movies
            </Text>
        </View>
    );

    const ButtonList: React.FC = () => (
        <View style={[
            styles.buttonContainer,
            isLandscape && styles.landscapeButtonContainer
        ]}>
            <NavigationButton
                title="Add Movies to DB"
                onPress={handleAddMoviesToDB}
                isPrimary
                isLoading={isAddingMovies}
            />
            <NavigationButton
                title="Search for Movies"
                onPress={() => navigation.navigate('MovieSearch')}
            />
            <NavigationButton
                title="Search for Actors"
                onPress={() => navigation.navigate('ActorSearch')}
            />
            <NavigationButton
                title="Extended Movie Search"
                onPress={() => navigation.navigate('ExtendedSearch')}
            />
        </View>
    );

    return (
        <SafeAreaView style={styles.container}>
            {/* Force re-render by using orientation in the key */}
            <View key={`orientation-${isLandscape}`} style={styles.content}>
                {isLandscape ? (
                    // Landscape Layout
                    <View style={styles.landscapeLayout}>
                        <View style={styles.landscapeLeft}>
                            <AppTitle />
                        </View>
                        <View style={styles.landscapeRight}>
                            <ButtonList />
                        </View>
                    </View>
                ) : (
                    // Portrait Layout
                    <View style={styles.portraitLayout}>
                        <AppTitle />
                        <ButtonList />
                    </View>
                )}
            </View>

            <FloatingSnackbar
                message={snackbarMessage}
                visible={showSnackbar}
                onDismiss={() => setShowSnackbar(false)}
            />
        </SafeAreaView>
    );
};

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#FAFAFA',
    },
    content: {
        flex: 1,
    },
    portraitLayout: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        padding: 24,
    },
    landscapeLayout: {
        flex: 1,
        flexDirection: 'row',
        alignItems: 'center',
        padding: 16,
    },
    landscapeLeft: {
        flex: 1,
        paddingRight: 24,
        justifyContent: 'center',
    },
    landscapeRight: {
        flex: 1,
        justifyContent: 'center',
    },
    titleContainer: {
        alignItems: 'center',
        marginBottom: 48,
    },
    landscapeTitleContainer: {
        alignItems: 'flex-start', // Align left in landscape
        marginBottom: 0,
    },
    appTitle: {
        fontSize: 36,
        fontWeight: '300',
        color: '#1565C0',
        marginBottom: 8,
        textAlign: 'center',
    },
    landscapeAppTitle: {
        fontSize: 32, // Slightly smaller in landscape
        textAlign: 'left',
    },
    appSubtitle: {
        fontSize: 16,
        color: '#666',
        textAlign: 'center',
    },
    landscapeAppSubtitle: {
        textAlign: 'left',
        fontSize: 14,
    },
    buttonContainer: {
        width: '100%',
        alignItems: 'center',
    },
    landscapeButtonContainer: {
        justifyContent: 'center',
        alignItems: 'stretch',
        width: '100%',
    },
    navigationButton: {
        backgroundColor: '#fff',
        paddingVertical: 16,
        paddingHorizontal: 24,
        borderRadius: 12,
        marginBottom: 16,
        width: '100%',
        alignItems: 'center',
        elevation: 2,
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.1,
        shadowRadius: 4,
        minHeight: 56,
        justifyContent: 'center',
    },
    landscapeButton: {
        marginBottom: 12, // Reduce spacing in landscape
        minHeight: 48,    // Smaller height in landscape
    },
    primaryButton: {
        backgroundColor: '#E3F2FD',
    },
    navigationButtonText: {
        fontSize: 16,
        fontWeight: '600',
        color: '#333',
    },
    primaryButtonText: {
        color: '#1565C0',
    },
});

export default MainScreen;
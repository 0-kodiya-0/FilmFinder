import React from 'react';
import {
    View,
    Text,
    TouchableOpacity,
    StyleSheet,
} from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { Ionicons } from '@expo/vector-icons';
import AppBar from './AppBar';
import { useOrientation } from '../hooks/useOrientation';

interface NetworkErrorScreenProps {
    onRetry: () => void;
    onNavigateBack?: () => void;
}

const NetworkErrorScreen: React.FC<NetworkErrorScreenProps> = ({
    onRetry,
    onNavigateBack,
}) => {
    const { isLandscape } = useOrientation();

    // Debug log
    console.log('NetworkErrorScreen orientation:', { isLandscape });

    const ErrorIcon = () => (
        <Ionicons
            name="warning"
            size={isLandscape ? 60 : 80}
            color="#f44336"
        />
    );

    const ErrorContent = () => (
        <View style={[styles.errorContent, isLandscape && styles.landscapeErrorContent]}>
            <Text style={[styles.title, isLandscape && styles.landscapeTitle]}>
                No Internet Connection
            </Text>

            <Text style={[styles.message, isLandscape && styles.landscapeMessage]}>
                Please check your network connection and try again.
            </Text>

            <View style={[styles.buttonContainer, isLandscape && styles.landscapeButtonContainer]}>
                <TouchableOpacity
                    style={[styles.retryButton, isLandscape && styles.landscapeRetryButton]}
                    onPress={onRetry}
                >
                    <Ionicons name="refresh" size={20} color="#fff" />
                    <Text style={styles.retryButtonText}>Retry</Text>
                </TouchableOpacity>

                <TouchableOpacity
                    style={[styles.reportButton, isLandscape && styles.landscapeReportButton]}
                >
                    <Ionicons name="send" size={20} color="#1565C0" />
                    <Text style={styles.reportButtonText}>Report Issue</Text>
                </TouchableOpacity>
            </View>
        </View>
    );

    return (
        <SafeAreaView style={styles.container}>
            {onNavigateBack && (
                <AppBar title="" onBackPress={onNavigateBack} />
            )}

            <View key={`network-error-${isLandscape}`} style={styles.content}>
                {isLandscape ? (
                    <View style={styles.landscapeLayout}>
                        <View style={styles.landscapeLeft}>
                            <ErrorIcon />
                        </View>
                        <View style={styles.landscapeRight}>
                            <ErrorContent />
                        </View>
                    </View>
                ) : (
                    <View style={styles.portraitLayout}>
                        <ErrorIcon />
                        <ErrorContent />
                    </View>
                )}
            </View>
        </SafeAreaView>
    );
};

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#fff',
    },
    content: {
        flex: 1,
        padding: 24,
    },
    portraitLayout: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
    },
    landscapeLayout: {
        flex: 1,
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'center',
    },
    landscapeLeft: {
        flex: 1,
        alignItems: 'center',
        justifyContent: 'center',
    },
    landscapeRight: {
        flex: 2,
        paddingLeft: 32,
    },
    errorContent: {
        alignItems: 'center',
        width: '100%',
    },
    landscapeErrorContent: {
        alignItems: 'flex-start', // Align left in landscape
    },
    title: {
        fontSize: 24,
        fontWeight: '600',
        color: '#333',
        marginTop: 24,
        marginBottom: 16,
        textAlign: 'center',
    },
    landscapeTitle: {
        fontSize: 20,
        marginTop: 0,
        marginBottom: 12,
        textAlign: 'left',
    },
    message: {
        fontSize: 16,
        color: '#666',
        textAlign: 'center',
        lineHeight: 24,
        marginBottom: 32,
    },
    landscapeMessage: {
        fontSize: 14,
        textAlign: 'left',
        marginBottom: 24,
    },
    buttonContainer: {
        width: '100%',
        alignItems: 'center',
    },
    landscapeButtonContainer: {
        alignItems: 'flex-start',
        flexDirection: 'row',
        flexWrap: 'wrap',
    },
    retryButton: {
        flexDirection: 'row',
        backgroundColor: '#E3F2FD',
        paddingHorizontal: 24,
        paddingVertical: 12,
        borderRadius: 8,
        alignItems: 'center',
        marginBottom: 16,
        minWidth: 200,
        justifyContent: 'center',
    },
    landscapeRetryButton: {
        minWidth: 140,
        marginRight: 16,
        marginBottom: 8,
    },
    retryButtonText: {
        color: '#1565C0',
        fontSize: 16,
        fontWeight: '600',
        marginLeft: 8,
    },
    reportButton: {
        flexDirection: 'row',
        borderWidth: 1,
        borderColor: '#1565C0',
        paddingHorizontal: 24,
        paddingVertical: 12,
        borderRadius: 8,
        alignItems: 'center',
        minWidth: 200,
        justifyContent: 'center',
    },
    landscapeReportButton: {
        minWidth: 140,
        marginBottom: 8,
    },
    reportButtonText: {
        color: '#1565C0',
        fontSize: 16,
        fontWeight: '600',
        marginLeft: 8,
    },
});

export default NetworkErrorScreen;
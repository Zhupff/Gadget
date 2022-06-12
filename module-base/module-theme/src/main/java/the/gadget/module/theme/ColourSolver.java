package the.gadget.module.theme;

/**
 * A color system built using CAM16 hue and chroma, and L* from L*a*b*.
 *
 * Thanks, and reference from https://github.com/material-foundation/material-color-utilities.
 * If there is any infringement, please tell me to delete this.
 */
final class ColourSolver {
    private ColourSolver() {}

    private static final double[][] LINRGB_FROM_SCALED_DISCOUNT = new double[][] {
        new double[] { 1373.2198709594231, -1100.4251190754821, -7.278681089101213 },
        new double[] { -271.815969077903 , 559.6580465940733  , -32.46047482791194 },
        new double[] { 1.9622899599665666, -57.173814538844006, 308.7233197812385 }};

    private static final double[][] SCALED_DISCOUNT_FROM_LINRGB = new double[][] {
        new double[] { 0.001200833568784504  , 0.002389694492170889 , 0.0002795742885861124 },
        new double[] { 0.0005891086651375999 , 0.0029785502573438758, 0.0003270666104008398 },
        new double[] { 0.00010146692491640572, 0.0005364214359186694, 0.0032979401770712076 }};

    private static final double[] CRITICAL_PLANES = new double[] {
        0.015176349177441876, 0.045529047532325624, 0.07588174588720938, 0.10623444424209313, 0.13658714259697685,
        0.16693984095186062 , 0.19729253930674434 , 0.2276452376616281 , 0.2579979360165119 , 0.28835063437139563,
        0.3188300904430532  , 0.350925934958123   , 0.3848314933096426 , 0.42057480301049466, 0.458183274052838  ,
        0.4976837250274023  , 0.5391024159806381  , 0.5824650784040898 , 0.6277969426914107 , 0.6751227633498623 ,
        0.7244668422128921  , 0.775853049866786   , 0.829304845476233  , 0.8848452951698498 , 0.942497089126609  ,
        1.0022825574869039  , 1.0642236851973577  , 1.1283421258858297 , 1.1946592148522128 , 1.2631959812511864 ,
        1.3339731595349034  , 1.407011200216447   , 1.4823302800086415 , 1.5599503113873272 , 1.6398909516233677 ,
        1.7221716113234105  , 1.8068114625156377  , 1.8938294463134073 , 1.9832442801866852 , 2.075074464868551  ,
        2.1693382909216234  , 2.2660538449872063  , 2.36523901573795   , 2.4669114995532007 , 2.5710888059345764 ,
        2.6777882626779785  , 2.7870270208169257  , 2.898822059350997  , 3.0131901897720907 , 3.1301480604002863 ,
        3.2497121605402226  , 3.3718988244681087  , 3.4967242352587946 , 3.624204428461639  , 3.754355295633311  ,
        3.887192587735158   , 4.022731918402185   , 4.160988767090289  , 4.301978482107941  , 4.445716283538092  ,
        4.592217266055746   , 4.741496401646282   , 4.893568542229298  , 5.048448422192488  , 5.20615066083972   ,
        5.3666897647573375  , 5.5300801301023865  , 5.696336044816294  , 5.865471690767354  , 6.037501145825082  ,
        6.212438385869475   , 6.390297286737924   , 6.571091626112461  , 6.7548350853498045 , 6.941541251256611  ,
        7.131223617812143   , 7.323895587840543   , 7.5195704746346665 , 7.7182615035334345 , 7.919981813454504  ,
        8.124744458384042   , 8.332562408825165   , 8.543448553206703  , 8.757415699253682  , 8.974476575321063  ,
        9.194643831691977   , 9.417930041841839   , 9.644347703669503  , 9.873909240696694  , 10.106627003236781 ,
        10.342513269534024  , 10.58158024687427   , 10.8238400726681   , 11.069304815507364 , 11.317986476196008 ,
        11.569896988756009  , 11.825048221409341  , 12.083451977536606 , 12.345119996613247 , 12.610063955123938 ,
        12.878295467455942  , 13.149826086772048  , 13.42466730586372  , 13.702830557985108 , 13.984327217668513 ,
        14.269168601521828  , 14.55736596900856   , 14.848930523210871 , 15.143873411576273 , 15.44220572664832  ,
        15.743938506781891  , 16.04908273684337   , 16.35764934889634  , 16.66964922287304  , 16.985093187232053 ,
        17.30399201960269   , 17.62635644741625   , 17.95219714852476  , 18.281524751807332 , 18.614349837764564 ,
        18.95068293910138   , 19.290534541298456  , 19.633915083172692 , 19.98083495742689  , 20.331304511189067 ,
        20.685334046541502  , 21.042933821039977  , 21.404114048223256 , 21.76888489811322  , 22.137256497705877 ,
        22.50923893145328   , 22.884842241736916  , 23.264076429332462 , 23.6469514538663   , 24.033477234264016 ,
        24.42366364919083   , 24.817520537484558  , 25.21505769858089  , 25.61628489293138  , 26.021211842414342 ,
        26.429848230738664  , 26.842203703840827  , 27.258287870275353 , 27.678110301598522 , 28.10168053274597  ,
        28.529008062403893  , 28.96010235337422   , 29.39497283293396  , 29.83362889318845  , 30.276079891419332 ,
        30.722335150426627  , 31.172403958865512  , 31.62629557157785  , 32.08401920991837  , 32.54558406207592  ,
        33.010999283389665  , 33.4802739966603    , 33.953417292456834 , 34.430438229418264 , 34.911345834551085 ,
        35.39614910352207   , 35.88485700094671   , 36.37747846067349  , 36.87402238606382  , 37.37449765026789  ,
        37.87891309649659   , 38.38727753828926   , 38.89959975977785  , 39.41588851594697  , 39.93615253289054  ,
        40.460400508064545  , 40.98864111053629   , 41.520882981230194 , 42.05713473317016  , 42.597404951718396 ,
        43.141702194811224  , 43.6900349931913    , 44.24241185063697  , 44.798841244188324 , 45.35933162437017  ,
        45.92389141541209   , 46.49252901546552   , 47.065252796817916 , 47.64207110610409  , 48.22299226451468  ,
        48.808024568002054  , 49.3971762874833    , 49.9904556690408   , 50.587870934119984 , 51.189430279724725 ,
        51.79514187861014   , 52.40501387947288   , 53.0190544071392   , 53.637271562750364 , 54.259673423945976 ,
        54.88626804504493   , 55.517063457223934  , 56.15206766869424  , 56.79128866487574  , 57.43473440856916  ,
        58.08241284012621   , 58.734331877617365  , 59.39049941699807  , 60.05092333227251  , 60.715611475655585 ,
        61.38457167773311   , 62.057811747619894  , 62.7353394731159   , 63.417162620860914 , 64.10328893648692  ,
        64.79372614476921   , 65.48848194977529   , 66.18756403501224  , 66.89098006357258  , 67.59873767827808  ,
        68.31084450182222   , 69.02730813691093   , 69.74813616640164  , 70.47333615344107  , 71.20291564160104  ,
        71.93688215501312   , 72.67524319850172   , 73.41800625771542  , 74.16517879925733  , 74.9167682708136   ,
        75.67278210128072   , 76.43322770089146   , 77.1981124613393   , 77.96744375590167  , 78.74122893956174  ,
        79.51947534912904   , 80.30219030335869   , 81.08938110306934  , 81.88105503125999  , 82.67721935322541  ,
        83.4778813166706    , 84.28304815182372   , 85.09272707154808  , 85.90692527145302  , 86.72564993000343  ,
        87.54890820862819   , 88.3767072518277    , 89.2090541872801   , 90.04595612594655  , 90.88742016217518  ,
        91.73345337380438   , 92.58406282226491   , 93.43925555268066  , 94.29903859396902  , 95.16341895893969  ,
        96.03240364439274   , 96.9059996312159    , 97.78421388448044  , 98.6670533535366   , 99.55452497210776  ,
    };

    /**
     * HCT, hue, chroma, and tone. A color system that provides a perceptually accurate color
     * measurement system that can also accurately render what colors will appear as in different
     * lighting environments.
     */
    static class HCT {

        static class Palette {
            final double hue;
            final double chrome;

            Palette(double hue, double chrome) {
                this.hue = hue;
                this.chrome = chrome;
            }

            int tone(int tone) { return solveToInt(this.hue, this.chrome, tone); }
        }
        final int argb;
        final double hue;
        final double chrome;
        final Palette a1;
        final Palette a2;
        final Palette a3;
        final Palette n1;
        final Palette n2;
        final Palette error;

        HCT(int argb) {
            int red   = (argb & 0x00ff0000) >> 16;
            int green = (argb & 0x0000ff00) >> 8;
            int blue  = (argb & 0x000000ff);
            double redL   = linearized(red);
            double greenL = linearized(green);
            double blueL  = linearized(blue);
            double x = 0.41233895 * redL + 0.35762064 * greenL + 0.18051042 * blueL;
            double y = 0.2126     * redL + 0.7152     * greenL + 0.0722     * blueL;
            double z = 0.01932141 * redL + 0.11916382 * greenL + 0.95034478 * blueL;
            double rT = (x * 0.401288)  + (y * 0.650173) + (z * -0.051461);
            double gT = (x * -0.250268) + (y * 1.204414) + (z * 0.045854);
            double bT = (x * -0.002079) + (y * 0.048952) + (z * 0.953127);
            double rD = 1.02117770275752   * rT;
            double gD = 0.9863077294280124 * gT;
            double bD = 0.9339605082802299 * bT;
            double rAF = Math.pow(0.3884814537800353 * Math.abs(rD) / 100.0, 0.42);
            double gAF = Math.pow(0.3884814537800353 * Math.abs(gD) / 100.0, 0.42);
            double bAF = Math.pow(0.3884814537800353 * Math.abs(bD) / 100.0, 0.42);
            double rA = Math.signum(rD) * 400.0 * rAF / (rAF + 27.13);
            double gA = Math.signum(gD) * 400.0 * gAF / (gAF + 27.13);
            double bA = Math.signum(bD) * 400.0 * bAF / (bAF + 27.13);

            double a = (11.0 * rA + -12.0 * gA + bA) / 11.0;
            double b = (rA + gA - 2.0 * bA) / 9.0;
            double u = (20.0 * rA + 20.0 * gA + 21.0 * bA) / 20.0;
            double p2 = (40.0 * rA + 20.0 * gA + bA) / 20.0;
            double atan2 = Math.atan2(b, a);
            double atanDegrees = Math.toDegrees(atan2);
            double hue = atanDegrees < 0 ? atanDegrees + 360.0 : atanDegrees >= 360 ? atanDegrees - 360.0 : atanDegrees;
            double ac = p2 * 1.0169191804458755;
            double j = 100.0 * Math.pow(ac / 29.980997194447333, 0.69 * 1.909169568483652);
            double huePrime = (hue < 20.14) ? hue + 360 : hue;
            double eHue = 0.25 * (Math.cos(Math.toRadians(huePrime) + 2.0) + 3.8);
            double p1 = 50000.0 / 13.0 * eHue * 1.0 * 1.0169191804458755;
            double t = p1 * Math.hypot(a, b) / (u + 0.305);
            double alpha = Math.pow(1.64 - Math.pow(0.29, 0.18418651851244416), 0.73) * Math.pow(t, 0.9);
            double chrome = alpha * Math.sqrt(j / 100.0);

            this.argb   = argb;
            this.hue    = hue;
            this.chrome = chrome;
            this.a1 = new Palette(this.hue, Math.max(48.0, this.chrome));
            this.a2 = new Palette(this.hue, 16.0);
            this.a3 = new Palette(this.hue + 60.0, 24.0);
            this.n1 = new Palette(this.hue, 4.0);
            this.n2 = new Palette(this.hue, 8.0);
            this.error = new Palette(25, 84.0);
        }
    }


    /**
     * Finds an sRGB color with the given hue, chroma, and L*, if possible.
     *
     * @param hueDegrees The desired hue, in degrees.
     * @param chroma The desired chroma.
     * @param lstar The desired L*.
     * @return A hexadecimal representing the sRGB color. The color has sufficiently close hue,
     *     chroma, and L* to the desired values, if possible; otherwise, the hue and L* will be
     *     sufficiently close, and chroma will be maximized.
     */
    private static int solveToInt(double hueDegrees, double chroma, double lstar) {
        double y = yFromLstar(lstar);
        if (chroma < 0.0001 || lstar < 0.0001 || lstar > 99.9999) {
            int component = delinearized(y);
            return argbFromRgb(component, component, component);
        }
        hueDegrees %= 360.0;
        if (hueDegrees < 0) { hueDegrees += 360.0; }
        double hueRadians = hueDegrees / 180 * Math.PI;
        int exactAnswer = findResultByJ(hueRadians, chroma, y);
        if (exactAnswer != 0) { return exactAnswer; }
        double[] linrgb = bisectToLimit(y, hueRadians);
        return argbFromLinrgb(linrgb);
    }

    /**
     * Finds a color with the given hue, chroma, and Y.
     *
     * @param hueRadians The desired hue in radians.
     * @param chroma The desired chroma.
     * @param y The desired Y.
     * @return The desired color as a hexadecimal integer, if found; 0 otherwise.
     */
    private static int findResultByJ(double hueRadians, double chroma, double y) {
        double j = Math.sqrt(y) * 11.0;
        double tInnerCoeff = 1 / Math.pow(1.64 - Math.pow(0.29, 0.18418651851244416), 0.73);
        double eHue = 0.25 * (Math.cos(hueRadians + 2.0) + 3.8);
        double p1 = eHue * (50000.0 / 13.0) * 1.0 * 1.0169191804458755;
        double hSin = Math.sin(hueRadians);
        double hCos = Math.cos(hueRadians);
        for (int iterationRound = 0; iterationRound < 5; iterationRound++) {
            double jNormalized = j / 100.0;
            double alpha = chroma == 0.0 || j == 0.0 ? 0.0 : chroma / Math.sqrt(jNormalized);
            double t = Math.pow(alpha * tInnerCoeff, 1.0 / 0.9);
            double ac = 29.980997194447333 * Math.pow(jNormalized, 1.0 / 0.69 / 1.909169568483652);
            double p2 = ac / 1.0169191804458755;
            double gamma = 23.0 * (p2 + 0.305) * t / (23.0 * p1 + 11 * t * hCos + 108.0 * t * hSin);
            double a = gamma * hCos;
            double b = gamma * hSin;
            double rA = (460.0 * p2 + 451.0 * a + 288.0  * b) / 1403.0;
            double gA = (460.0 * p2 - 891.0 * a - 261.0  * b) / 1403.0;
            double bA = (460.0 * p2 - 220.0 * a - 6300.0 * b) / 1403.0;
            double rCScaled = inverseChromaticAdaptation(rA);
            double gCScaled = inverseChromaticAdaptation(gA);
            double bCScaled = inverseChromaticAdaptation(bA);
            double[] linrgb = matrixMultiply(new double[] { rCScaled, gCScaled, bCScaled }, LINRGB_FROM_SCALED_DISCOUNT);
            if (linrgb[0] < 0 || linrgb[1] < 0 || linrgb[2] < 0) { return 0; }
            double kR = 0.2126;
            double kG = 0.7152;
            double kB = 0.0722;
            double fnj = kR * linrgb[0] + kG * linrgb[1] + kB * linrgb[2];
            if (fnj <= 0) { return 0; }
            if (iterationRound == 4 || Math.abs(fnj - y) < 0.002) {
                if (linrgb[0] > 100.01 || linrgb[1] > 100.01 || linrgb[2] > 100.01) { return 0; }
                return argbFromLinrgb(linrgb);
            }
            j = j - (fnj - y) * j / (2 * fnj);
        }
        return 0;
    }

    /**
     * Finds a color with the given Y and hue on the boundary of the cube.
     *
     * @param y The Y value of the color.
     * @param targetHue The hue of the color.
     * @return The desired color, in linear RGB coordinates.
     */
    private static double[] bisectToLimit(double y, double targetHue) {
        double[][] segment = bisectToSegment(y, targetHue);
        double[] left = segment[0];
        double leftHue = hueOf(left);
        double[] right = segment[1];
        for (int axis = 0; axis < 3; axis++) {
            if (left[axis] != right[axis]) {
                int lPlane = -1;
                int rPlane = 255;
                if (left[axis] < right[axis]) {
                    lPlane = (int) Math.floor(trueDelinearized(left[axis]) - 0.5);
                    rPlane = (int) Math.ceil(trueDelinearized(right[axis]) - 0.5);
                } else {
                    lPlane = (int) Math.ceil(trueDelinearized(left[axis]) - 0.5);
                    rPlane = (int) Math.floor(trueDelinearized(right[axis]) - 0.5);
                }
                for (int i = 0; i < 8; i++) {
                    if (Math.abs(rPlane - lPlane) <= 1) {
                        break;
                    } else {
                        int mPlane = (int) Math.floor((lPlane + rPlane) / 2.0);
                        double midPlaneCoordinate = CRITICAL_PLANES[mPlane];
                        double[] mid = setCoordinate(left, midPlaneCoordinate, right, axis);
                        double midHue = hueOf(mid);
                        if ((targetHue - leftHue + Math.PI * 8) % (Math.PI * 2) < (midHue - leftHue + Math.PI * 8) % (Math.PI * 2)) {
                            right = mid;
                            rPlane = mPlane;
                        } else {
                            left = mid;
                            leftHue = midHue;
                            lPlane = mPlane;
                        }
                    }
                }
            }
        }
        return new double[] { (left[0] + right[0]) / 2, (left[1] + right[1]) / 2, (left[2] + right[2]) / 2 };
    }

    /**
     * Finds the segment containing the desired color.
     *
     * @param y The Y value of the color.
     * @param targetHue The hue of the color.
     * @return A list of two sets of linear RGB coordinates, each corresponding to an endpoint of the
     *     segment containing the desired color.
     */
    private static double[][] bisectToSegment(double y, double targetHue) {
        double[] left = new double[] {-1.0, -1.0, -1.0};
        double[] right = left;
        double leftHue = 0.0;
        double rightHue = 0.0;
        boolean initialized = false;
        boolean uncut = true;
        for (int n = 0; n < 12; n++) {
            double[] mid = nthVertex(y, n);
            if (mid[0] < 0) { continue; }
            double midHue = hueOf(mid);
            if (!initialized) {
                left = mid;
                right = mid;
                leftHue = midHue;
                rightHue = midHue;
                initialized = true;
                continue;
            }
            if (uncut || (midHue - leftHue + Math.PI * 8) % (Math.PI * 2) < (rightHue - leftHue + Math.PI * 8) % (Math.PI * 2)) {
                uncut = false;
                if ((targetHue - leftHue + Math.PI * 8) % (Math.PI * 2) < (midHue - leftHue + Math.PI * 8) % (Math.PI * 2)) {
                    right = mid;
                    rightHue = midHue;
                } else {
                    left = mid;
                    leftHue = midHue;
                }
            }
        }
        return new double[][] {left, right};
    }

    /**
     * Returns the hue of a linear RGB color in CAM16.
     *
     * @param linrgb The linear RGB coordinates of a color.
     * @return The hue of the color in CAM16, in radians.
     */
    private static double hueOf(double[] linrgb) {
        double[] scaledDiscount = matrixMultiply(linrgb, SCALED_DISCOUNT_FROM_LINRGB);
        double rA = chromaticAdaptation(scaledDiscount[0]);
        double gA = chromaticAdaptation(scaledDiscount[1]);
        double bA = chromaticAdaptation(scaledDiscount[2]);
        // redness-greenness
        double a = (11.0 * rA + -12.0 * gA + bA) / 11.0;
        // yellowness-blueness
        double b = (rA + gA - 2.0 * bA) / 9.0;
        return Math.atan2(b, a);
    }

    /**
     * Intersects a segment with a plane.
     *
     * @param source The coordinates of point A.
     * @param coordinate The R-, G-, or B-coordinate of the plane.
     * @param target The coordinates of point B.
     * @param axis The axis the plane is perpendicular with. (0: R, 1: G, 2: B)
     * @return The intersection point of the segment AB with the plane R=coordinate, G=coordinate, or
     *     B=coordinate
     */
    private static double[] setCoordinate(double[] source, double coordinate, double[] target, int axis) {
        double t = (coordinate - source[axis]) / (target[axis] - source[axis]);
        return new double[] {
            source[0] + (target[0] - source[0]) * t,
            source[1] + (target[1] - source[1]) * t,
            source[2] + (target[2] - source[2]) * t};
    }

    /**
     * Returns the nth possible vertex of the polygonal intersection.
     *
     * @param y The Y value of the plane.
     * @param n The zero-based index of the point. 0 <= n <= 11.
     * @return The nth possible vertex of the polygonal intersection of the y plane and the RGB cube,
     *     in linear RGB coordinates, if it exists. If this possible vertex lies outside of the cube,
     *     [-1.0, -1.0, -1.0] is returned.
     */
    private static double[] nthVertex(double y, int n) {
        double kR = 0.2126;
        double kG = 0.7152;
        double kB = 0.0722;
        double coordA = n % 4 <= 1 ? 0.0 : 100.0;
        double coordB = n % 2 == 0 ? 0.0 : 100.0;
        if (n < 4) {
            double g = coordA;
            double b = coordB;
            double r = (y - g * kG - b * kB) / kR;
            return (0.0 <= r && r <= 100.0) ? new double[] { r, g, b } : new double[] { -1.0, -1.0, -1.0 };
        } else if (n < 8) {
            double b = coordA;
            double r = coordB;
            double g = (y - r * kR - b * kB) / kG;
            return (0.0 <= g && g <= 100.0) ? new double[] { r, g, b } : new double[] { -1.0, -1.0, -1.0 };
        } else {
            double r = coordA;
            double g = coordB;
            double b = (y - r * kR - g * kG) / kB;
            return (0.0 <= b && b <= 100.0) ? new double[] { r, g, b } : new double[] { -1.0, -1.0, -1.0 };
        }
    }

    /** Converts a color from linear RGB components to ARGB format. */
    private static int argbFromLinrgb(double[] linrgb) {
        int r = delinearized(linrgb[0]);
        int g = delinearized(linrgb[1]);
        int b = delinearized(linrgb[2]);
        return argbFromRgb(r, g, b);
    }

    /** Converts a color from RGB components to ARGB format. */
    private static int argbFromRgb(int red, int green, int blue) {
        return (255 << 24) | ((red & 255) << 16) | ((green & 255) << 8) | (blue & 255);
    }

    /**
     * Converts an L* value to a Y value.
     *
     * <p>L* in L*a*b* and Y in XYZ measure the same quantity, luminance.
     *
     * <p>L* measures perceptual luminance, a linear scale. Y in XYZ measures relative luminance, a
     * logarithmic scale.
     *
     * @param lstar L* in L*a*b*
     * @return Y in XYZ
     */
    private static double yFromLstar(double lstar) {
        double base = (lstar + 16.0) / 116.0;
        double base3 = base * base * base;
        return 100.0 * (base3 > 216.0 / 24389.0 ? base3 : (116.0 * base - 16) / 24389.0 / 27.0);
    }

    /** Multiplies a 1x3 row vector with a 3x3 matrix. */
    private static double[] matrixMultiply(double[] row, double[][] matrix) {
        double a = row[0] * matrix[0][0] + row[1] * matrix[0][1] + row[2] * matrix[0][2];
        double b = row[0] * matrix[1][0] + row[1] * matrix[1][1] + row[2] * matrix[1][2];
        double c = row[0] * matrix[2][0] + row[1] * matrix[2][1] + row[2] * matrix[2][2];
        return new double[] {a, b, c};
    }

    /**
     * Linearizes an RGB component.
     *
     * @param rgbComponent 0 <= rgb_component <= 255, represents R/G/B channel
     * @return 0.0 <= output <= 100.0, color channel converted to linear RGB space
     */
    private static double linearized(int rgbComponent) {
        double normalized = rgbComponent / 255.0;
        return normalized <= 0.040449936 ? normalized / 12.92 * 100.0 : Math.pow((normalized + 0.055) / 1.055, 2.4) * 100.0;
    }

    /**
     * Delinearizes an RGB component.
     *
     * @param rgbComponent 0.0 <= rgb_component <= 100.0, represents linear R/G/B channel
     * @return 0 <= output <= 255, color channel converted to regular RGB space
     */
    private static int delinearized(double rgbComponent) {
        return Math.min(255, Math.max(0, (int) Math.round(trueDelinearized(rgbComponent))));
    }

    /**
     * Delinearizes an RGB component, returning a floating-point number.
     *
     * @param rgbComponent 0.0 <= rgb_component <= 100.0, represents linear R/G/B channel
     * @return 0.0 <= output <= 255.0, color channel converted to regular RGB space
     */
    private static double trueDelinearized(double rgbComponent) {
        double normalized = rgbComponent / 100.0;
        return 255.0 * (normalized <= 0.0031308 ? normalized * 12.92 : 1.055 * Math.pow(normalized, 1.0 / 2.4) - 0.055);
    }

    private static double chromaticAdaptation(double component) {
        double af = Math.pow(Math.abs(component), 0.42);
        return (component < 0.0 ? -1.0 : component == 0.0 ? 0.0 : 1.0) * 400.0 * af / (af + 27.13);
    }

    private static double inverseChromaticAdaptation(double adapted) {
        double adaptedAbs = Math.abs(adapted);
        double base = Math.max(0, 27.13 * adaptedAbs / (400.0 - adaptedAbs));
        return (adapted < 0.0 ? -1.0 : adapted == 0.0 ? 0.0 : 1.0) * Math.pow(base, 1.0 / 0.42);
    }
}
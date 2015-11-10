package org.cateproject.domain.constants;

import java.util.HashSet;
import java.util.Set;

public enum DCMIType {
	StillImage, Sound, MovingImage;

        private Set<MultimediaFileType> multimediaFileTypes = new HashSet<MultimediaFileType>();

        static {
            DCMIType.StillImage.multimediaFileTypes.add(MultimediaFileType.original);
            DCMIType.StillImage.multimediaFileTypes.add(MultimediaFileType.large);
            DCMIType.StillImage.multimediaFileTypes.add(MultimediaFileType.thumbnail);

            DCMIType.MovingImage.multimediaFileTypes.add(MultimediaFileType.original);
            DCMIType.MovingImage.multimediaFileTypes.add(MultimediaFileType.large);
            DCMIType.MovingImage.multimediaFileTypes.add(MultimediaFileType.thumbnail);

            DCMIType.Sound.multimediaFileTypes.add(MultimediaFileType.original);
        }

        public Set<MultimediaFileType> getMultimediaFileTypes() {
            return multimediaFileTypes;
        }
}
